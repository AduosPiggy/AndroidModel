package com.example.androidmodel.tools.dexfix.simple.util;

import android.util.Log;

import com.example.androidmodel.tools.dexfix.simple.bean.AnnotationsOff;
import com.example.androidmodel.tools.dexfix.simple.bean.ClassDataItem;
import com.example.androidmodel.tools.dexfix.simple.bean.ClassDefItem;
import com.example.androidmodel.tools.dexfix.simple.bean.CodeItem;
import com.example.androidmodel.tools.dexfix.simple.bean.CodeItemBin;
import com.example.androidmodel.tools.dexfix.simple.bean.DexFixBusiness;
import com.example.androidmodel.tools.dexfix.simple.bean.Fields;
import com.example.androidmodel.tools.dexfix.simple.bean.MapItem;
import com.example.androidmodel.tools.dexfix.simple.bean.Methods;
import com.example.androidmodel.tools.dexfix.simple.bean.StaticValues;
import com.example.androidmodel.tools.dexfix.simple.exception.DexException;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;

/**
 * @author kfflso
 * @data 2024/10/17 15:03
 * @plus:
 * Dex文件格式https://source.android.google.cn/docs/core/runtime/dex-format?hl=zh-cn#parameter-annotation
 */
public class DexFixPlusUtils implements DexFixBusiness {
//    private static final Logger log = LoggerFactory.getLogger(DexFixPlusUtils.class);
    private final String TAG = "DexFixPlusUtils";
    private final String dexPath;
    private final String[] binPaths;
    private final String fixPath;
    private ByteBuffer dexBuffer;//dump 下来的 dex 文件的数据
    private ByteBuffer fixBuffer;//用来存储修复 dex 的数据
    private List<MapItem> mapItemList = new ArrayList<>();
    private List<ClassDefItem> classDefItemList = new ArrayList<>();

    private int index_header_item_in_map_item_list                  = 0;
    private int index_string_id_item_in_map_item_list               = 0;
    private int index_type_id_item_in_map_item_list                 = 0;
    private int index_proto_id_item_in_map_item_list                = 0;
    private int index_field_id_item_in_map_item_list                = 0;
    private int index_method_id_item_in_map_item_list               = 0;
    private int index_class_def_item_in_map_item_list               = 0;
    private int index_map_list_in_map_item_list                     = 0;
    private int index_type_list_in_map_item_list                    = 0;
    private int index_annotation_set_ref_list_in_map_item_list      = 0;
    private int index_annotation_set_item_in_map_item_list          = 0;
    private int index_class_data_item_in_map_item_list              = 0;
    private int index_code_item_in_map_item_list                    = 0;
    private int index_string_data_item_in_map_item_list             = 0;
    private int index_debug_info_item_in_map_item_list              = 0;
    private int index_annotation_item_in_map_item_list              = 0;
    private int index_encode_array_item_in_map_item_list            = 0;
    private int index_annotation_directory_item_in_map_item_list    = 0;


    public DexFixPlusUtils(String dexPath, String[] binPaths) {
        this.dexPath = dexPath;
        this.binPaths = binPaths;
        this.fixPath = dexPath.endsWith(".dex") ? dexPath.replaceAll("\\.dex$", "_repaired_plus.dex") : dexPath + "_repaired_plus.dex";
        dexBuffer = ByteBuffer.wrap(IoUtils.readFile(dexPath));
        fixBuffer = ByteBuffer.allocate(dexBuffer.capacity());
        dexBuffer.order(ByteOrder.LITTLE_ENDIAN);//小端字节序
        fixBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * 1.修复 code_item 前面所有数据
     * 2.修复 code_item
     * 3.修复 修复 code_item 后面所有数据
     * 4.修复 map_item_list 的偏移量
     * 5.修复 其他偏移量
     * 6.修复 checksum
     * 7.修复 hash
     */
    public void fixDex(){
        try{
            fixMagic();
            fixMethod();
            fixHash();
            clear();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private boolean fixMagic(){
        dexBuffer.position(0);
        byte[] magics = readByteBufferToByte(dexBuffer,0,MAGIC_LEN);
        if(magics != null && magics.length == MAGIC_LEN){
            String str_dex = "" + (char)magics[0] + (char)magics[1] + (char)magics[2];
            String str_03x = "" + (magics[4]-48) + (magics[5]-48) + (magics[6]-48);
            if(str_dex.equals("dex") && str_03x.startsWith("03")){
                return writeBBToBB(dexBuffer,0,MAGIC_LEN,fixBuffer,0);
            }
        }
        magics = DEX_MOCK_MAGIC;
        if(writeBytesToBB(magics,dexBuffer,0)){
            return writeBBToBB(dexBuffer,0,MAGIC_LEN,fixBuffer,0);
        }
        return false;
    }

    //修复code_item 前的数据
    /*
    * 1.修复 code_item 前的数据
    * 2.修复 code_item
    * 3.修复 code_item 后的数据
    * 4.修复 code_item 后的所有相关偏移值
    *   (1) struct map_list_type dex_map_list -> map_item
    *   (2) TYPE_STRING_DATA_ITEM -> struct string_id_list dex_string_ids -> uint string_data_off
    *   (3) TYPE_DEBUG_INFO_ITEM ->
    */
    private void fixMethod(){
        //读取bin文件中的codeItem,费时间,考虑提前加载
        List<CodeItemBin> codeItemBinList = convertCodeItemList(binPaths);
        HashMap<Integer, CodeItemBin> codeItemBinHashMap = new HashMap<>();
        for(CodeItemBin codeItemBin : codeItemBinList){
            codeItemBinHashMap.put(codeItemBin.getMethodIndex(), codeItemBin);
        }
        codeItemBinList.clear();

        //读取文件头
        dexBuffer.position(0x20);
        int file_size = dexBuffer.getInt();
        int header_size = dexBuffer.getInt();
        int endian_tag = dexBuffer.getInt();
        int link_size = dexBuffer.getInt();
        int link_off = dexBuffer.getInt();
        int map_off = dexBuffer.getInt();
        int string_ids_size = dexBuffer.getInt();
        int string_ids_off = dexBuffer.getInt();
        int type_ids_size = dexBuffer.getInt();
        int type_ids_off = dexBuffer.getInt();
        int proto_ids_size = dexBuffer.getInt();
        int proto_ids_off = dexBuffer.getInt();
        int field_ids_size = dexBuffer.getInt();
        int field_ids_off = dexBuffer.getInt();
        int method_ids_size = dexBuffer.getInt();
        int method_ids_off = dexBuffer.getInt();
        int class_defs_size = dexBuffer.getInt();//class_def 的 size;
        int class_defs_off = dexBuffer.getInt();//class_def_off 起始偏移位置的值
        int data_size = dexBuffer.getInt();
        int data_off = dexBuffer.getInt();
//        int total_dex_len = data_off + data_size;//11859948 = 1597420 + 10262528

        //读取 map_item_list;以及对应的index;

        Log.d(TAG,"start initMapItemList");
        mapItemList.clear();
        dexBuffer.position(map_off);
        int map_item_list_size = dexBuffer.getInt();
        for(int a = 0; a < map_item_list_size; a++){
            MapItem mapItem = new MapItem();
            int this_type_start = dexBuffer.position();
            short type = dexBuffer.getShort();
            short unused =dexBuffer.getShort();
            int size = dexBuffer.getInt();
            int this_type_offset_start = dexBuffer.position();
            int offset = dexBuffer.getInt();
            mapItem.setType(type);
            mapItem.setUnused(unused);
            mapItem.setSize(size);
            mapItem.setOffset(offset);
            mapItem.setThis_type_start(this_type_start);
            mapItem.setThis_type_offset_start(this_type_offset_start);
            mapItem.setIndex_of_map_item_list(a);
            //初始化type 在 map_item_list 中的 index;
            switch (type){
                case TYPE_HEADER_ITEM :
                    index_header_item_in_map_item_list = a;
                    continue;
                case TYPE_STRING_ID_ITEM :
                    index_string_id_item_in_map_item_list = a;
                    continue;
                case TYPE_TYPE_ID_ITEM :
                    index_type_id_item_in_map_item_list = a;
                    continue;
                case TYPE_PROTO_ID_ITEM :
                    index_proto_id_item_in_map_item_list = a;
                    continue;
                case TYPE_FIELD_ID_ITEM :
                    index_field_id_item_in_map_item_list = a;
                    continue;
                case TYPE_METHOD_ID_ITEM :
                    index_method_id_item_in_map_item_list = a;
                    continue;
                case TYPE_CLASS_DEF_ITEM :
                    index_class_def_item_in_map_item_list = a;
                    continue;
                case TYPE_MAP_LIST :
                    index_map_list_in_map_item_list = a;
                    continue;
                case TYPE_TYPE_LIST :
                    index_type_list_in_map_item_list = a;
                    continue;
                case TYPE_ANNOTATION_SET_REF_LIST :
                    index_annotation_set_ref_list_in_map_item_list = a;
                    continue;
                case TYPE_ANNOTATION_SET_ITEM :
                    index_annotation_set_item_in_map_item_list = a;
                    continue;
                case TYPE_CLASS_DATA_ITEM :
                    index_class_data_item_in_map_item_list = a;
                    continue;
                case TYPE_CODE_ITEM :
                    index_code_item_in_map_item_list = a;
                    mapItem.setNeed_fix_offset(1);
                    continue;
                case TYPE_STRING_DATA_ITEM :
                    index_string_data_item_in_map_item_list = a;
                    continue;
                case TYPE_DEBUG_INFO_ITEM :
                    index_debug_info_item_in_map_item_list = a;
                    continue;
                case TYPE_ANNOTATION_ITEM :
                    index_annotation_item_in_map_item_list = a;
                    continue;
                case TYPE_ENCODED_ARRAY_ITEM :
                    index_encode_array_item_in_map_item_list = a;
                    continue;
                case TYPE_ANNOTATION_DIRECTORY_ITEM :
                    index_annotation_directory_item_in_map_item_list = a;
                    continue;
            }
            mapItemList.add(mapItem);
        }
        for(MapItem mapItem : mapItemList){
            if(index_code_item_in_map_item_list < mapItem.getIndex_of_map_item_list()){
                mapItem.setNeed_fix_offset(0);
                continue;
            }
            if(index_code_item_in_map_item_list > mapItem.getIndex_of_map_item_list()){
                mapItem.setNeed_fix_offset(2);
                continue;
            }
            mapItem.setNeed_fix_offset(1);
        }
        Log.d(TAG,"finished initMapItemList");
        dexBuffer.position(map_off);


        //1.修复 code_item 前的数据
        int position_map_item_code_item_offset = mapItemList.get(index_code_item_in_map_item_list).getOffset();
        writeBBToBB(dexBuffer,0,position_map_item_code_item_offset,fixBuffer,0);

        //2.修复 code_item
        //主要修复 class_data_off + class_data_item class_data(重点是code_item)
//start
        int total_len_diff = 0;// += bin ins.length - codeItem.length
        int cur_class_def_off = class_defs_off;
        classDefItemList.clear();
        Log.d(TAG,"start class_def");
        for(int a=0; a < class_defs_size; a++){
            Log.d(TAG,"=============================================================================================");
            ClassDefItem classDefItem = new ClassDefItem();
            dexBuffer.position(cur_class_def_off);
            int class_def_item_start = cur_class_def_off;
            int class_idx = dexBuffer.getInt();
            int access_flags = dexBuffer.getInt();
            int superclass_idx = dexBuffer.getInt();
            int interfaces_off_start = dexBuffer.position();
            int interfaces_off = dexBuffer.getInt();
            int source_file_idx = dexBuffer.getInt();
            int annotations_off_start = dexBuffer.position();
            int annotations_off = dexBuffer.getInt();
            int class_data_off_start = dexBuffer.position();
            int class_data_off = dexBuffer.getInt();
            int static_values_off_start = dexBuffer.position();
            int static_values_off = dexBuffer.getInt();

            classDefItem.setClass_idx(class_idx);
            classDefItem.setAccess_flags(access_flags);
            classDefItem.setSuperclass_idx(superclass_idx);
            classDefItem.setInterfaces_off(interfaces_off);
            classDefItem.setSource_file_idx(source_file_idx);
            classDefItem.setAnnotations_off(annotations_off);
            classDefItem.setClass_data_off(class_data_off);
            classDefItem.setStatic_values_off(static_values_off);

            classDefItem.setClass_def_item_start(class_def_item_start);
            classDefItem.setInterfaces_off_start(interfaces_off_start);
            classDefItem.setAnnotations_off_start(annotations_off_start);
            classDefItem.setClass_data_off_start(class_data_off_start);
            classDefItem.setStatic_values_off_start(static_values_off_start);

            Log.d(TAG,"start interfaces_off: " + interfaces_off);
            if(interfaces_off != 0){


            }


            Log.d(TAG,"start annotations_off: " + annotations_off);
            if(annotations_off != 0){
                AnnotationsOff annotationsOff = new AnnotationsOff();
                dexBuffer.position(annotations_off);
                int class_annotations_off = dexBuffer.getInt();
                int fields_size = dexBuffer.getInt();
                int methods_size = dexBuffer.getInt();
                int parameters_size = dexBuffer.getInt();
                int annotations_off_length = 4*4 + fields_size*8 + methods_size*8 + parameters_size *8;

                annotationsOff.setClass_annotation_off(class_annotations_off);
                annotationsOff.setFields_size(fields_size);
                annotationsOff.setMethods_size(methods_size);
                annotationsOff.setParameters_size(parameters_size);
                annotationsOff.setAnnotations_off_length(annotations_off_length);
                classDefItem.setAnnotationsOff(annotationsOff);
            }


            Log.d(TAG,"start class_data_off: " + class_data_off);
            if(class_data_off != 0){
                ClassDataItem classDataItem = new ClassDataItem();
                dexBuffer.position(class_data_off);
                int staticFieldsSize = Leb128Utils.readULeb128(dexBuffer);
                int instanceFieldsSize = Leb128Utils.readULeb128(dexBuffer);
                int directMethodsSize = Leb128Utils.readULeb128(dexBuffer);
                int virtualMethodsSize = Leb128Utils.readULeb128(dexBuffer);

                classDataItem.setStaticFieldsSize(staticFieldsSize);
                classDataItem.setInstanceFieldsSize(instanceFieldsSize);
                classDataItem.setDirectMethodsSize(directMethodsSize);
                classDataItem.setVirtualMethodsSize(virtualMethodsSize);

                //修复size才能查看code_item;
                fixBuffer.position(class_data_off + total_len_diff);
                byte[] bytes_staticFieldsSize   = writeUnsignedLeb128(staticFieldsSize);
                byte[] bytes_instanceFieldsSize = writeUnsignedLeb128(instanceFieldsSize);
                byte[] bytes_directMethodsSize  = writeUnsignedLeb128(directMethodsSize);
                byte[] bytes_virtualMethodsSize = writeUnsignedLeb128(virtualMethodsSize);
                fixBuffer.put(bytes_staticFieldsSize);
                fixBuffer.put(bytes_instanceFieldsSize);
                fixBuffer.put(bytes_directMethodsSize);
                fixBuffer.put(bytes_virtualMethodsSize);


                if(staticFieldsSize != 0){
                    List<Fields> fieldsList = new ArrayList<>();
                    int fieldsSizeStart = dexBuffer.position();
                    int fieldsSizeLength = 0;
                    int field_index = 0;
                    for(int b = 0; b < staticFieldsSize; b++){
                        Log.d(TAG,"class_def/class_defs_size: " + a+"/"+class_defs_size + "; static_field/staticFieldsSize: " + b+"/"+staticFieldsSize);
                        int position = dexBuffer.position();
                        int _field_idx_diff = Leb128Utils.readULeb128(dexBuffer);
                        int _access_flags = Leb128Utils.readULeb128(dexBuffer);
                        field_index += _field_idx_diff;
                        dexBuffer.position(position);
                        int _field_length = Leb128Utils.readULeb128Count(dexBuffer) + Leb128Utils.readULeb128Count(dexBuffer);
                        fieldsSizeLength += _field_length;

                        Fields fields = new Fields();
                        fields.setField_idx_diff(_field_idx_diff);
                        fields.setAccess_flags(_access_flags);
                        fields.setField_index(field_index);
                        fields.setField_start(position);
                        fields.setField_length(_field_length);
                        fieldsList.add(fields);
                    }
                    classDataItem.setStaticFieldsList(fieldsList);
                    classDataItem.setStaticFieldsSizeStart(fieldsSizeStart);
                    classDataItem.setStaticFieldsSizeLength(fieldsSizeLength);
                }

                if(instanceFieldsSize != 0){
                    List<Fields> fieldsList = new ArrayList<>();
                    int fieldsSizeStart = dexBuffer.position();
                    int fieldsSizeLength = 0;
                    int field_index = 0;
                    for(int b = 0; b < instanceFieldsSize; b++){
                        Log.d(TAG,"class_def/class_defs_size: " + a+"/"+class_defs_size + "; instance_field/instanceFieldsSize: " + b+"/"+instanceFieldsSize);
                        int position = dexBuffer.position();
                        int _field_idx_diff = Leb128Utils.readULeb128(dexBuffer);
                        int _access_flags = Leb128Utils.readULeb128(dexBuffer);
                        field_index += _field_idx_diff;
                        dexBuffer.position(position);
                        int _field_length = Leb128Utils.readULeb128Count(dexBuffer) + Leb128Utils.readULeb128Count(dexBuffer);
                        fieldsSizeLength += _field_length;

                        Fields fields = new Fields();
                        fields.setField_idx_diff(_field_idx_diff);
                        fields.setAccess_flags(_access_flags);
                        fields.setField_index(field_index);
                        fields.setField_start(position);
                        fields.setField_length(_field_length);

                        fieldsList.add(fields);
                    }
                    classDataItem.setInstanceFieldsSizeStart(fieldsSizeStart);
                    classDataItem.setInstanceFieldsSizeLength(fieldsSizeLength);
                }

                if(directMethodsSize != 0){
                    List<Methods> methodsList = new ArrayList<>();
                    int directMethodsSizeStart = dexBuffer.position();
                    int methodsSizeLength = 0;
                    int method_index = 0;
                    for (int b=0; b < directMethodsSize; b++){
                        Log.d(TAG,"class_def/class_defs_size: " + a+"/"+class_defs_size + "; direct_method/directMethodsSize: " + b+"/"+directMethodsSize);
                        Methods methods = new Methods();
                        int method_start = dexBuffer.position();
                        int _method_idx_diff = Leb128Utils.readULeb128(dexBuffer);
                        int _access_flags = Leb128Utils.readULeb128(dexBuffer);
                        int _code_off_start = dexBuffer.position();
                        int _code_off = Leb128Utils.readULeb128(dexBuffer);
                        method_index += _method_idx_diff;
                        dexBuffer.position(method_start);
                        int _method_idx_diff_length = Leb128Utils.readULeb128Count(dexBuffer);
                        int _access_flags_length = Leb128Utils.readULeb128Count(dexBuffer);
                        int _code_off_length = Leb128Utils.readULeb128Count(dexBuffer);
                        int method_length = _method_idx_diff_length + _access_flags_length + _code_off_length;

                        if(_code_off != 0) {
                            CodeItem codeItem = new CodeItem();
                            int position = dexBuffer.position();
                            dexBuffer.position(_code_off);
                            int registers_size = dexBuffer.getShort() & 0xffff;
                            int ins_size = dexBuffer.getShort() & 0xffff;
                            int outs_size = dexBuffer.getShort() & 0xffff;
                            int tries_size = dexBuffer.getShort() & 0xffff;
                            int debug_info_off_start = dexBuffer.position();
                            int debug_info_off = dexBuffer.getInt();
                            int instructions_size = dexBuffer.getInt();
                            short[] instructions = readUnsignedShortArray(instructions_size);
                            int code_item_start = _code_off;
                            int code_item_length = calCodeItemLen(instructions_size, tries_size, dexBuffer);
                            methodsSizeLength += code_item_length;
                            dexBuffer.position(position);

                            codeItem.setRegister_size(registers_size);
                            codeItem.setIns_size(ins_size);
                            codeItem.setOuts_size(outs_size);
                            codeItem.setTries_size(tries_size);
                            codeItem.setDebug_info_off(debug_info_off);
                            codeItem.setInstructions_size(instructions_size);
                            codeItem.setInstructions(instructions);
                            codeItem.setDebug_info_off_start(debug_info_off_start);
                            codeItem.setCode_item_start(code_item_start);
                            codeItem.setCode_item_length(code_item_length);

                            methods.setCode_off(_code_off);
                            methods.setCodeItem(codeItem);

                            //修复code_item
                            CodeItemBin codeItemBin = codeItemBinHashMap.get(method_index);
                            if(codeItemBin == null || codeItemBin.getInsns() == null){
                                writeBBToBB(dexBuffer,code_item_start,code_item_length,fixBuffer,code_item_start + total_len_diff);
                                Log.d(TAG,"dm_method_index: " + method_index + "; codeItem == null || codeItem.getInsns() == null");
                                continue;
                            }
                            int bin_insns_length = codeItemBin.getInsnsLength();
                            byte[] binBytes = codeItemBin.getInsns();
                            total_len_diff += bin_insns_length - code_item_length;
                            int fix_code_item_start = code_item_start + total_len_diff;
                            fixBuffer.position(fix_code_item_start);
                            fixBuffer.put(binBytes);
                            //todo 修复记录code_off的偏移值 -> 应该在所有的code_item修复完成,计算出total_len_diff时再修复
//                            fixBuffer.position(code_item_start);
//                            //修复methods的code_off, 即code_item起始的偏移地址;
//                            byte[] byte_fix_code_item_start = writeUnsignedLeb128(fix_code_item_start);
//                            if(byte_fix_code_item_start.length < _code_off_length){
//                                byte_fix_code_item_start = Arrays.copyOf(byte_fix_code_item_start,_code_off_length);
//                            }
//                            fixBuffer.put(byte_fix_code_item_start);
                        }

                        methods.setMethod_idx_diff(_method_idx_diff);
                        methods.setAccess_flags(_access_flags);
                        methods.setMethod_index(method_index);
                        methods.setMethod_start(method_start);
                        methods.setMethod_length(method_length);
                        methods.setCode_off_start(_code_off_start);

                        methodsList.add(methods);

                    }
                    classDataItem.setDirectMethodsSizeStart(directMethodsSizeStart);
                    classDataItem.setDirectMethodsSizeLength(methodsSizeLength);
                }

                if(virtualMethodsSize != 0){
                    List<Methods> methodsList = new ArrayList<>();
                    int virtualMethodSizeStart = dexBuffer.position();
                    int methodsSizeLength = 0;
                    int method_index = 0;
                    for(int b=0; b < virtualMethodsSize; b++){
                        Log.d(TAG,"class_def/class_defs_size: " + a+"/"+class_defs_size + "; virtual_method/virtualMethodsSize: " + b+"/"+virtualMethodsSize);
                        Methods methods = new Methods();
                        int method_start = dexBuffer.position();
                        int _method_idx_diff = Leb128Utils.readULeb128(dexBuffer);
                        int _access_flags = Leb128Utils.readULeb128(dexBuffer);
                        int _code_off_start = dexBuffer.position();
                        int _code_off = Leb128Utils.readULeb128(dexBuffer);
                        method_index += _method_idx_diff;
                        dexBuffer.position(method_start);
                        int _method_idx_diff_length = Leb128Utils.readULeb128Count(dexBuffer);
                        int _access_flags_length = Leb128Utils.readULeb128Count(dexBuffer);
                        int _code_off_length = Leb128Utils.readULeb128Count(dexBuffer);
                        int method_length = _method_idx_diff_length + _access_flags_length + _code_off_length;
                        if(_code_off != 0) {
                            int position = dexBuffer.position();
                            CodeItem codeItem = new CodeItem();
                            dexBuffer.position(_code_off);
                            int registers_size = dexBuffer.getShort() & 0xffff;
                            int ins_size = dexBuffer.getShort() & 0xffff;
                            int outs_size = dexBuffer.getShort() & 0xffff;
                            int tries_size = dexBuffer.getShort() & 0xffff;
                            int debug_info_off_start = dexBuffer.position();
                            int debug_info_off = dexBuffer.getInt();
                            int instructions_size = dexBuffer.getInt();
                            short[] instructions = readUnsignedShortArray(instructions_size);
                            int code_item_start = _code_off;
                            int code_item_length = calCodeItemLen(instructions_size, tries_size, dexBuffer);
                            methodsSizeLength += code_item_length;
                            dexBuffer.position(position);

                            codeItem.setRegister_size(registers_size);
                            codeItem.setIns_size(ins_size);
                            codeItem.setOuts_size(outs_size);
                            codeItem.setTries_size(tries_size);
                            codeItem.setDebug_info_off(debug_info_off);
                            codeItem.setInstructions_size(instructions_size);
                            codeItem.setInstructions(instructions);
                            codeItem.setDebug_info_off_start(debug_info_off_start);
                            codeItem.setCode_item_start(code_item_start);
                            codeItem.setCode_item_length(code_item_length);

                            methods.setCode_off(_code_off);
                            methods.setCodeItem(codeItem);

                            //修复code_item
                            CodeItemBin codeItemBin = codeItemBinHashMap.get(method_index);
                            if(codeItemBin == null || codeItemBin.getInsns() == null){
                                writeBBToBB(dexBuffer,code_item_start,code_item_length,fixBuffer,code_item_start + total_len_diff);
                                Log.d(TAG,"dm_method_index: " + method_index + "; codeItem == null || codeItem.getInsns() == null");
                                continue;
                            }
                            int bin_insns_length = codeItemBin.getInsnsLength();
                            byte[] binBytes = codeItemBin.getInsns();
                            total_len_diff += bin_insns_length - code_item_length;
                            int fix_code_item_start = code_item_start + total_len_diff;
                            fixBuffer.position(fix_code_item_start);
                            fixBuffer.put(binBytes);
                            //todo 修复记录code_off的偏移值 -> 应该在所有的code_item修复完成,计算出total_len_diff时再修复
//                            fixBuffer.position(code_item_start);
//                            //修复methods的code_off, 即code_item起始的偏移地址;
//                            byte[] byte_fix_code_item_start = writeUnsignedLeb128(fix_code_item_start);
//                            if(byte_fix_code_item_start.length < _code_off_length){
//                                byte_fix_code_item_start = Arrays.copyOf(byte_fix_code_item_start,_code_off_length);
//                            }
//                            fixBuffer.put(byte_fix_code_item_start);
                        }

                        methods.setMethod_idx_diff(_method_idx_diff);
                        methods.setAccess_flags(_access_flags);
                        methods.setMethod_index(method_index);
                        methods.setMethod_start(method_start);
                        methods.setMethod_length(method_length);
                        methods.setCode_off_start(_code_off_start);

                        methodsList.add(methods);

                    }
                    classDataItem.setVirtualMethodsSizeStart(virtualMethodSizeStart);
                    classDataItem.setVirtualMethodsSizeLength(methodsSizeLength);
                }

                classDefItem.setClassDataItem(classDataItem);
            }

            Log.d(TAG,"start static_values_off: " + static_values_off);
            if(static_values_off != 0){
                StaticValues staticValues = new StaticValues();
                dexBuffer.position(static_values_off);
                int total_length = 0;
                int size = Leb128Utils.readULeb128(dexBuffer);
                dexBuffer.position(static_values_off);
                total_length += Leb128Utils.readULeb128Count(dexBuffer);
                for(int b=0; b<size; b++){
                    byte valueTypeAndArg = dexBuffer.get();
                    total_length += 1;
                    int valueType = valueTypeAndArg & 0x1f;
                    int valueArg = (valueTypeAndArg & 0xe0) >> 5;
                    int tmp_length = getEncodeValuesLength(valueType);
                    total_length += tmp_length;
                }
                staticValues.setSize(size);
                staticValues.setTotal_length(total_length);

                classDefItem.setStaticValues(staticValues);
            }

            classDefItemList.add(classDefItem);
            cur_class_def_off += 0x20;
        }
        Log.d(TAG,"end class_def");
//end

        // code_item_end dexBuffer.position()=7127604 -> 6CC234
        //3.修复 code_item 后的数据
        int position_next_map_item_code_item_offset = mapItemList.get(index_code_item_in_map_item_list+1).getOffset();
        int len_from_after_code_item_to_dex_end = file_size - position_next_map_item_code_item_offset;
        int position_fix_next_map_item_code_item_offset = position_next_map_item_code_item_offset + total_len_diff;
        writeBBToBB(dexBuffer,position_next_map_item_code_item_offset,len_from_after_code_item_to_dex_end,fixBuffer,position_fix_next_map_item_code_item_offset);

        //4 修复 code_item 后的所有相关偏移值
        //4.1 修复map_item中的偏移值
        //4.2 修复header中相关偏移值
        //4.3 修复具体位置的偏移值
        for(MapItem mapItem : mapItemList){
            if(mapItem.getNeed_fix_offset() == 2){
                int type = (int) mapItem.getType();
                //修复map_item中的偏移值
                int fix_offset = mapItem.getOffset() + total_len_diff;
                fixBuffer.position(mapItem.getThis_type_offset_start());
                fixBuffer.putInt(fix_offset);
                //修复具体位置的偏移值
                switch ( type ){
                    case TYPE_MAP_LIST:
                        fixBuffer.position(0x34);
                        fixBuffer.putInt(fix_offset);
                        break;
                    default:
                        break;
                }
                fix_offset = 0;
            }
        }

        // 4.4 修复 struct string_id_list dex_string_ids -> struct string_id_item string_id[x] -> uint string_data_off
        //修复struct string_id_list dex_string_ids -> offset
        int cur_string_data_off = 0x70;
        for(int a=0; a<string_ids_size; a++){
            dexBuffer.position(cur_string_data_off);
            int value_string_data_off = dexBuffer.getInt();
            int fix_value_string_data_off = value_string_data_off + total_len_diff;
            fixBuffer.position(cur_string_data_off);
            fixBuffer.putInt(fix_value_string_data_off);
            cur_string_data_off += 4;
        }

        // 4.5 修复 classDefItem 中相关偏移值
        for(int a=0; a < classDefItemList.size(); a++){
            ClassDefItem classDefItem = classDefItemList.get(a);
            int interfaces_off = classDefItem.getInterfaces_off();
            int annotations_off = classDefItem.getAnnotations_off();
            int class_data_off = classDefItem.getClass_data_off();
            int static_values_off = classDefItem.getStatic_values_off();

            if(interfaces_off != 0){
                // 修复class_def interfaces_off
                int interfaces_off_start = classDefItem.getInterfaces_off_start();
                int fix_interfaces_off_start = interfaces_off_start + total_len_diff;
                int fix_interfaces_off = interfaces_off + total_len_diff;
                fixBuffer.position(fix_interfaces_off_start);
                fixBuffer.putInt(fix_interfaces_off);
            }
            if(annotations_off != 0){
                // 修复class_def annotations_off
                int annotations_off_start = classDefItem.getAnnotations_off_start();
                int fix_annotations_off_start = annotations_off_start + total_len_diff;
                int fix_annotations_off = annotations_off + total_len_diff;
                fixBuffer.position(fix_annotations_off_start);
                fixBuffer.putInt(fix_annotations_off);
                // 修复 data 区数据 // 考虑不在for循环中处理
                // 考虑在读取数据的时候计算总长度,然后一次性写入;
                int length = classDefItem.getAnnotations_off_total_length();
                writeBBToBB(dexBuffer,annotations_off,length,fixBuffer,fix_annotations_off);
            }
            //code_item已经在读取数据时候构建到fixBuffer
            if(class_data_off != 0){

            }
            if(static_values_off != 0){
                int static_values_off_start = classDefItem.getStatic_values_off_start();
                int total_length = classDefItem.getStaticValues().getTotal_length();
                int fix_static_values_off_start = static_values_off_start + total_len_diff;
                writeBBToBB(dexBuffer,static_values_off_start,total_length,fixBuffer,fix_static_values_off_start);
            }

        }

//        //主要修复 annotations_off + annotations_directory_item annotations 和 static_values_off + static_values
//        cur_class_def_off = class_defs_off;
//        int cur_annotation_off = cur_class_def_off + 20;
//        for(int a=0; a<class_defs_size; a++){
//            dexBuffer.position(cur_annotation_off);
//            int annotations_off = dexBuffer.getInt();
//            int class_data_off = dexBuffer.getInt();
//            int static_values_off = dexBuffer.getInt();
//            if(static_values_off!=0){
//                int total_length = 0;
//                dexBuffer.position(static_values_off);
//                int size11 = readUleb128();
//                total_length += 1;
//                for(int b=0;b<size11;b++){
//                    byte valueTypeAndArg = dexBuffer.get();
//                    total_length += 1;
//                    int valueType = valueTypeAndArg & 0x1f;
//                    int valueArg = (valueTypeAndArg & 0xe0) >> 5;
////                    int valueType = (valueTypeAndArg >> 3) & 0x1F; // value_type: 前 5 位
////                    int valueArg = valueTypeAndArg & 0x07; // value_arg: 后 3 位
//                    int tmp_length = getEncodeValuesLength(valueType);
//                    total_length += tmp_length;
//                }
//                writeBBToBB(dexBuffer,static_values_off,total_length,fixBuffer,static_values_off+total_len_diff);
//            }
//
//
//            if(annotations_off!=0){
//                int total_length = 0;
//                dexBuffer.position(annotations_off);
//                int class_annotations_off = dexBuffer.getInt();
//                int fields_size = dexBuffer.getInt();
//                int methods_size = dexBuffer.getInt();
//                int parameters_size = dexBuffer.getInt();
//                //struct annotation_set_item class_annotations 1A87BCh
//                //fields_size
//                total_length += 8*fields_size;
//                //methods_size
//                total_length += 8*methods_size;
//                //parameters_size
//                total_length += 8*parameters_size;
//                dexBuffer.position(annotations_off+total_length);
//                writeBBToBB(dexBuffer,annotations_off,total_length,fixBuffer,annotations_off+total_len_diff);
//                int zzzzz = 0;
//                if(parameters_size >0){
//                    zzzzz=2;
//                }
//            }
//
//            cur_class_def_off = cur_class_def_off + 32;//0x20
//            cur_annotation_off = cur_class_def_off + 20;
//        }

    }

    private int initMapItemList(int map_off,List<MapItem> mapItemList){
        Log.d(TAG,"start initMapItemList");
        mapItemList.clear();
        dexBuffer.position(map_off);
        int map_item_list_size = dexBuffer.getInt();
        int index_code_item_in_map_item_list = 0;
        for(int a = 0; a < map_item_list_size; a++){
            MapItem mapItem = new MapItem();
            int this_type_start = dexBuffer.position();
            short type = dexBuffer.getShort();
            short unused =dexBuffer.getShort();
            int size = dexBuffer.getInt();
            int this_type_offset_start = dexBuffer.position();
            int offset = dexBuffer.getInt();
            mapItem.setType(type);
            mapItem.setUnused(unused);
            mapItem.setSize(size);
            mapItem.setOffset(offset);
            mapItem.setThis_type_start(this_type_start);
            mapItem.setThis_type_offset_start(this_type_offset_start);
            mapItem.setIndex_of_map_item_list(a);
            if(type == TYPE_CODE_ITEM){
                index_code_item_in_map_item_list = a;
                mapItem.setNeed_fix_offset(1);
            }
            mapItemList.add(mapItem);
        }
        for(MapItem mapItem : mapItemList){
            if(index_code_item_in_map_item_list < mapItem.getIndex_of_map_item_list()){
                mapItem.setNeed_fix_offset(0);
                continue;
            }
            if(index_code_item_in_map_item_list == mapItem.getIndex_of_map_item_list()){
                continue;
            }
            mapItem.setNeed_fix_offset(2);
        }
        Log.d(TAG,"finished initMapItemList");
        return index_code_item_in_map_item_list;
    }

    private void fixHash(){
        int len = fixBuffer.capacity();
        byte[] fixBytes = new byte[len];
        fixBuffer.position(0);
        fixBuffer.get(fixBytes,0,len);
        // 更新签名
        if(!fixSignature(fixBytes)){
            return;
        }
        // 更新校验和
        if(!fixChecksum(fixBytes)){
            return;
        }
        fixHeaderFileSize(fixBytes,len);
        // 将更新后的数据写回文件
        IoUtils.writeFile(fixPath, fixBytes);
    }

    private void fixHeaderFileSize(byte[] bytes,int size){
        bytes[33] = (byte) (size & 0xFF);        // 低字节
        bytes[34] = (byte) ((size >> 8) & 0xFF);
        bytes[35] = (byte) ((size >> 16) & 0xFF);
        bytes[36] = (byte) ((size >> 24) & 0xFF);
    }
    private void clear(){
        dexBuffer.clear();
        fixBuffer.clear();
    }
    //计算dex的code_item的长度;
    private int calCodeItemLen(int instructionsSize, int triesSize, ByteBuffer byteBuffer){
        int code_item_length = 0;
        int instructionsLength = instructionsSize * 2;
        int padding = 0;
        int tryItemLength = 0;
        int catchHandlerLength = 0;
        if(triesSize > 0 ){
            if(instructionsLength % 4 != 0){
                padding = 2;
                byteBuffer.position(byteBuffer.position()+padding);
            }
            tryItemLength = triesSize * 8;
            byteBuffer.position(byteBuffer.position() + tryItemLength);
            int catchHandlersSize = readUleb128();
            catchHandlerLength += 1;
            for(int n = 0; n < catchHandlersSize; n++){
                int curPosition = byteBuffer.position();
                catchHandlerLength += readSLeb128_count();
                byteBuffer.position(curPosition);
                int size = readSLeb128();
                int handlersCount = Math.abs(size);
                for (int o = 0; o < handlersCount; o++) {
                    catchHandlerLength += readUleb128_count();// int[] typeIndexes
                    catchHandlerLength += readUleb128_count();// int[] addresses
                }
                if(size <= 0){
                    catchHandlerLength += readUleb128_count();
                }
            }
        }
        code_item_length = 16 + instructionsLength + padding + tryItemLength + catchHandlerLength; // dumped.dex 中 code_item 的 len;
        return code_item_length;
    }


    //getEncodeValuesLength 没有被测试过
    //https://source.android.google.cn/docs/core/runtime/dex-format?hl=zh-cn#field-id-item
    private int getEncodeValuesLength( int valueType ){
        switch(valueType){
            case VALUE_BYTE :
                return 1;
            case VALUE_SHORT :
                return 1;
            case VALUE_CHAR :
                return 1;
            case VALUE_INT :
                return 2;
            case VALUE_LONG :
                return 4;
            case VALUE_FLOAT :
                return 2;
            case VALUE_DOUBLE :
                return 4;
            case VALUE_METHOD_TYPE :
                return 2;
            case VALUE_METHOD_HANDLE :
                return 2;
            case VALUE_STRING :
                return 2;
            case VALUE_TYPE :
                return 2;
            case VALUE_FIELD :
                return 2;
            case VALUE_METHOD :
                return 2;
            case VALUE_ENUM :
                return 2;
            case VALUE_NULL :
                return 1;
            case VALUE_BOOLEAN :
                return 1;

//            case VALUE_ARRAY :
//            case VALUE_ANNOTATION :
        }
        return 0;
    }

    /**
     * Converts an integer to a byte array in little-endian unsigned LEB128 format.
     *
     * @param number the integer to convert
     * @return a byte array representing the LEB128 encoded value
     */
    private byte[] writeUnsignedLeb128(int number) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int remaining = number >>> 7;
        while (remaining != 0) {
            outputStream.write((number & 0x7F) | 0x80); // Set the continuation bit
            number = remaining;
            remaining >>>= 7;
        }
        outputStream.write(number & 0x7F); // Write the last byte
        return outputStream.toByteArray();
    }

    /**
     * Converts an integer to a byte array in little-endian signed LEB128 format.
     *
     * @param number the integer to convert
     * @return a byte array representing the signed LEB128 encoded value
     */
    private byte[] writeSignedLeb128(int number) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int value = number;
        boolean more = true;
        while (more) {
            int byteValue = value & 0x7F; // Get the lower 7 bits
            value >>= 7; // Shift right by 7 bits
            // Determine if we need to set the continuation bit
            if ((number < 0 && value == 0 && (byteValue & 0x40) == 0) || // Negative case
                    (number > 0 && value == -1 && (byteValue & 0x40) != 0)) { // Positive case
                byteValue |= 0x40; // Set the sign bit
            }
            if (value == 0) {
                more = false;
            } else {
                byteValue |= 0x80; // Set the continuation bit
            }
            outputStream.write(byteValue);
        }
        return outputStream.toByteArray();
    }
    private String decimalToHex8(int number){
        return String.format("%08x",number);
    }
    /**
     *
     * @param dexData 用codeItem替换过的dexData
     * @return 更新签名信息到 dexData 12-32字节
     */
    private boolean fixSignature(byte[] dexData) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(dexData, 32, dexData.length - 32);
            int amt = md.digest(dexData, 12, 20);
            if(amt != 20) {
                return false;
            }
        } catch(DigestException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param dexData 用codeItem替换过的dexData
     * @return 更新校验和 到 dexData 8-11字节
     */
    private boolean fixChecksum(byte[] dexData) {
        try{
            if(dexData == null || dexData.length < 12){
                return false;
            }
            Adler32 a32 = new Adler32();
            a32.update(dexData, 12, dexData.length - 12);
            int sum = (int)a32.getValue();
            dexData[8] = (byte)sum;
            dexData[9] = (byte)(sum >> 8);
            dexData[10] = (byte)(sum >> 16);
            dexData[11] = (byte)(sum >> 24);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public short[] readUnsignedShortArray(int length) {
        if (length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] result = new short[length];
        for (int i = 0; i < length; i++) {
            result[i] = (short) (dexBuffer.getShort() & 0xffff);
        }
        return result;
    }

    private void readFields(int count) {
        int fieldIndex = 0;
        for (int i = 0; i < count; i++) {
            fieldIndex += readUleb128(); // field index diff
            int accessFlags = readUleb128();
            int a = 0;
        }
    }

    /**
     * Reads an signed integer from {@code in}.
     */
    private int readSLeb128() {
        int result = 0;
        int cur;
        int count = 0;
        int signBits = -1;
        do {
            cur = dexBuffer.get() & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            signBits <<= 7;
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);
        if ((cur & 0x80) == 0x80) {
            throw new DexException("invalid LEB128 sequence");
        }
        // Sign extend if appropriate
        if (((signBits >> 1) & result) != 0 ) {
            result |= signBits;
        }
        return result;
    }
    /**
     * Reads an unsigned LEB128 integer from the given ByteBuffer starting at the specified offset.
     */
    private int readUleb128() {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = dexBuffer.get() & 0xff; // 读取当前字节并转换为无符号整型
            result |= (cur & 0x7f) << (count * 7); // 将低7位添加到结果中
            count++;
        } while ((cur & 0x80) == 0x80 && count < 5); // 如果最高位是1，继续读取下一个字节

        // 检查是否未正确结束 LEB128 序列
        if ((cur & 0x80) == 0x80) {
            throw new IllegalArgumentException("Invalid LEB128 sequence");
        }
        return result;
    }
    /**
     * Reads an signed integer from {@code in}.
     */
    private int readSLeb128_count() {
        int result = 0;
        int cur;
        int count = 0;
        int signBits = -1;
        do {
            cur = dexBuffer.get() & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            signBits <<= 7;
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);
        if ((cur & 0x80) == 0x80) {
            throw new DexException("invalid LEB128 sequence");
        }
        // Sign extend if appropriate
        if (((signBits >> 1) & result) != 0 ) {
            result |= signBits;
        }
        return count;
    }
    /**
     * Reads an unsigned LEB128 integer from the given ByteBuffer starting at the specified offset.
     */
    private int readUleb128_count() {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = dexBuffer.get() & 0xff; // 读取当前字节并转换为无符号整型
            result |= (cur & 0x7f) << (count * 7); // 将低7位添加到结果中
            count++;
        } while ((cur & 0x80) == 0x80 && count < 5); // 如果最高位是1，继续读取下一个字节

        // 检查是否未正确结束 LEB128 序列
        if ((cur & 0x80) == 0x80) {
            throw new IllegalArgumentException("Invalid LEB128 sequence");
        }
        return count;
    }

    private boolean writeBBToBB(ByteBuffer inBuffer, int in_offset, int in_len, ByteBuffer outBuffer, int out_offset){
        byte[] bytes = readByteBufferToByte(inBuffer,in_offset,in_len);
        return writeBytesToBB(bytes,outBuffer,out_offset);
    }
    private boolean writeBytesToBB(byte[] bytes, ByteBuffer dest, int offset) {

        if (bytes == null || offset < 0) {
            return false;
        }
        int neededCapacity = offset + bytes.length;
        if(dest == null){
            dest = ByteBuffer.allocate(neededCapacity);
        }else {
            if(neededCapacity > dest.capacity()){
                ByteBuffer newBuffer = ByteBuffer.allocate(neededCapacity);
                dest.flip();// 反转当前缓冲区以便从头开始写入
                newBuffer.put(dest);
                dest = newBuffer;
            }
        }
        dest.position(offset);
        dest.put(bytes);
        return true;
    }
    private byte[] readByteBufferToByte(ByteBuffer buffer, int offset, int len){
        int needCapacity = offset + len;
        if(offset<0 || len<=0 || buffer == null || needCapacity > buffer.capacity()){
            return null;
        }
        byte[] bytes = new byte[len];
        int curPosition = buffer.position();
        buffer.position(offset);
        buffer.get(bytes,0,len);
        buffer.position(curPosition);
        return bytes;
    }

    //bin文件数据之一示例: {name:void javax.annotation.MatchesPattern$Checker.<init>(),method_idx:393,offset:78032,code_item_len:24,ins:AQABAAEAAACqogMABAAAAHAQRAEAAA4A};
    private List<CodeItemBin> convertCodeItemList(String[] binPaths) {
        List<CodeItemBin> codeItemBinList = new ArrayList<>();
        for(String binPath : binPaths){
            codeItemBinList.addAll(collectCodeItems(binPath));
        }
        codeItemBinList.sort(Comparator.comparingInt(CodeItemBin::getOffset));
        return codeItemBinList;
    }

    private List<CodeItemBin> collectCodeItems(String binPath){
        List<CodeItemBin> codeItemBinList = new ArrayList<>();
        String input = new String(IoUtils.readFile(binPath));
        Pattern pattern = Pattern.compile("\\{name:(.+?),method_idx:(\\d+),offset:(\\d+),code_item_len:(\\d+),ins:(.+?)\\}");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            String methodName = matcher.group(1);
            int methodIndex = Integer.parseInt(matcher.group(2));
            int offset = Integer.parseInt(matcher.group(3));
            int insnsLength = Integer.parseInt(matcher.group(4));
            String insBase64 = matcher.group(5);
            byte[] insns = Base64.getDecoder().decode(insBase64);
            CodeItemBin codeItemBin = new CodeItemBin(methodName,methodIndex,offset,insnsLength,insns);
            codeItemBinList.add(codeItemBin);
        }
        return codeItemBinList;
    }



}
