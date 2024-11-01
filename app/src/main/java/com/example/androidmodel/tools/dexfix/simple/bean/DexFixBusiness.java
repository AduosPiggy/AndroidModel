package com.example.androidmodel.tools.dexfix.simple.bean;

/**
 * @author kfflso
 * @data 2024/10/15 13:56
 * @plus:
 */
public interface DexFixBusiness {
    public static final String DEX_END = ".dex";
    public static final String BIN_END = ".bin";
    public static final String F_DEXPATH_NULL = "fix dex fail for dexPath is null";
    public static final String F_DEXPATH_END  = "fix dex fail for dexPath not endWith " + DEX_END;
    public static final String F_BINPATH_NULL = "fix dex fail for binPath is null";
    public static final String F_BINPATH_END  = "fix dex fail for binPath not endWith " + BIN_END;
    public static final String F_DEX_NOTEXISTS  = "fix dex fail for dex file not exists ";
    public static final String F_BIN_NOTEXISTS  = "fix dex fail for bin file not exists ";
    public static final String F_DEX_LENERR  = "fix dex fail for dex file length <= 0 ";
    public static final String F_BIN_LENERR  = "fix dex fail for dex file length <= 0 ";
    public static final String PATH_FINE = "dexPath and binPath check result is fine";

    public static final String F_MAGIC = "fix dex magic fail";
    public static final String F_METHOD = "fix dex method codeItem fail";
    public static final String F_HASH = "fix dex hash fail";
    public static final String FIX_SUCCESS = "fix dex success";



    /** DexFixPlusUtils*/
    public static final int HEADER_MAGIC_OFF = 0x0;
    public static final int HEADER_CHECKSUM_OFF = 0x8;
    public static final int HEADER_SIGNATURE_OFF = 0xC;
    public static final int HEADER_CLASSDEFS_SIZE_OFF = 0x60;
    public static final int HEADER_CLASSDEFS_OFF = 0x64;
    public static final int HEADER_DATA_SIZE = 0X68;
    public static final int HEADER_DATA_OFF = 0x6c;

    public static final int HEADER_LEN = 0x70;
    public static final int MAGIC_LEN = 0x8;




    //dex文件魔术头格式修复; dex.035
    public static final byte[] DEX_MOCK_MAGIC = {0x64, 0x65, 0x78, 0x0a, 0x30, 0x33, 0x35, 0x00};
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];

    public static final int VALUE_BYTE = 0x00;
    public static final int VALUE_SHORT = 0x02;
    public static final int VALUE_CHAR = 0x03;
    public static final int VALUE_INT = 0x04;
    public static final int VALUE_LONG = 0x06;
    public static final int VALUE_FLOAT = 0x10;
    public static final int VALUE_DOUBLE = 0x11;
    public static final int VALUE_METHOD_TYPE = 0x15;
    public static final int VALUE_METHOD_HANDLE = 0x16;
    public static final int VALUE_STRING = 0x17;
    public static final int VALUE_TYPE = 0x18;
    public static final int VALUE_FIELD = 0x19;
    public static final int VALUE_METHOD = 0x1A;
    public static final int VALUE_ENUM = 0x1B;
    public static final int VALUE_ARRAY = 0x1C;
    public static final int VALUE_ANNOTATION = 0x1D;
    public static final int VALUE_NULL = 0x1E;
    public static final int VALUE_BOOLEAN = 0x1F;


//    public static final short TYPE_HEADER_ITEM      = 0x0000;
//    public static final short TYPE_STRING_ID_ITEM   = 0x0001;
//    public static final short TYPE_TYPE_ID_ITEM     = 0x0002;
//    public static final short TYPE_PROTO_ID_ITEM    = 0x0003;
//    public static final short TYPE_FIELD_ID_ITEM    = 0x0004;
//    public static final short TYPE_METHOD_ID_ITEM   = 0x0005;
//    public static final short TYPE_CLASS_DEF_ITEM   = 0x0006;
    public static final int TYPE_MAP_LIST         = 0x1000;
    public static final int TYPE_TYPE_LIST        = 0x1001;
    public static final int TYPE_ANNOTATION_SET_REF_LIST = 0x1002;
    public static final int TYPE_ANNOTATION_SET_ITEM = 0x1003;
    public static final int TYPE_CLASS_DATA_ITEM  = 0x2000;
    public static final int TYPE_CODE_ITEM        = 0x2001;
    public static final int TYPE_STRING_DATA_ITEM = 0x2002;
    public static final int TYPE_DEBUG_INFO_ITEM  = 0x2003;
    public static final int TYPE_ANNOTATION_ITEM  = 0x2004;
    public static final int TYPE_ENCODED_ARRAY_ITEM = 0x2005;
    public static final int TYPE_ANNOTATION_DIRECTORY_ITEM = 0x2006;



}
