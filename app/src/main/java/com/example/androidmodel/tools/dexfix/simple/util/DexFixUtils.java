package com.example.androidmodel.tools.dexfix.simple.util;

import com.example.androidmodel.tools.dexfix.simple.bean.CodeItemBin;
import com.example.androidmodel.tools.dexfix.simple.bean.DexFixBusiness;

import java.io.RandomAccessFile;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;

/**
 * @author kfflso
 * @data 2024/10/15 10:59
 * @plus:
 */
public class DexFixUtils implements DexFixBusiness {
    //dumped dex filepath
    private String dexPath;
    //dumped bin filepath
    private String[] binPaths;
    //fixed dex filepath
    private String fixedPath;

    public DexFixUtils(String dexPath, String[] binPaths) {
        this.dexPath = dexPath;
        this.binPaths = binPaths;
        this.fixedPath = dexPath.endsWith(".dex") ? dexPath.replaceAll("\\.dex$", "_repaired.dex") : dexPath + "_repaired.dex";
    }

    public String fixDex(){
        byte[] dumpedDexBytes = IoUtils.readFile(dexPath);
        IoUtils.writeFile(fixedPath, dumpedDexBytes);
        if(!fixDexMagic()){
            return F_MAGIC;
        }
        if( !fixMethodBody() ){
            return F_METHOD;
        }
        if(!fixDexHashes()){
            return F_HASH;
        }
        return FIX_SUCCESS;
    }

    private boolean fixDexMagic() {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(fixedPath, "rw");
            randomAccessFile.seek(0);
            int firstPartOfMagic = randomAccessFile.readInt();
            int secondPartOfMagic = randomAccessFile.readInt();

            if (firstPartOfMagic == 0 || secondPartOfMagic == 0) {
                randomAccessFile.seek(0);
                randomAccessFile.write(DEX_MOCK_MAGIC);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            IoUtils.close(randomAccessFile);
        }
        return true;
    }

    //覆盖式修复:1.copy dump 下来的dex文件 到 _repair.dex文件中;2.将 bin 文件中的方法体(ins)根据 offset 覆盖式的写入 _repair.dex 文件中;
    private boolean fixMethodBody(){
        RandomAccessFile raf_fixedDex = null;
        try {
            raf_fixedDex = new RandomAccessFile(fixedPath,"rw");
            List<CodeItemBin> codeItemBinList = convertCodeItemList(binPaths);
            for(CodeItemBin codeItemBin : codeItemBinList){
                raf_fixedDex.seek(codeItemBin.getOffset());
                raf_fixedDex.write(codeItemBin.getInsns());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            IoUtils.close(raf_fixedDex);
        }
        return true;
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

    private boolean fixDexHashes() {
        // 读取 DEX 数据
        byte[] dexData = IoUtils.readFile(fixedPath);
        // 更新签名
        if(!fixSignature(dexData)){
            return false;
        }
        // 更新校验和
        if(!fixChecksum(dexData)){
            return false;
        }
        // 将更新后的数据写回文件
        IoUtils.writeFile(fixedPath, dexData);
        return true;
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

}
