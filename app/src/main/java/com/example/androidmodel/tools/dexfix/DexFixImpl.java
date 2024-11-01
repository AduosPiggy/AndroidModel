package com.example.androidmodel.tools.dexfix;


import com.example.androidmodel.tools.dexfix.simple.bean.DexFixBusiness;
import com.example.androidmodel.tools.dexfix.simple.util.DexFixPlusUtils;
import com.example.androidmodel.tools.dexfix.simple.util.DexFixUtils;

import java.io.File;

/**
 * @author kfflso
 * @data 2024/10/15 11:50
 * @plus:
 *
 * 测试文件:
 * 通过网盘分享的文件：zzz_dex_11859948.zip
 * 链接: https://pan.baidu.com/s/1YvJFXorSsYIlQ0ya_MJ4oA?pwd=mxyg 提取码: mxyg
 * 通过网盘分享的文件：zzz_dex_11859948.zip
 * 链接: https://pan.baidu.com/s/1YvJFXorSsYIlQ0ya_MJ4oA?pwd=mxyg 提取码: mxyg
 * 解压后,将以上文件挨个放在 /data/local/tmp  目录下
 *
 */
public class DexFixImpl implements DexFixBusiness {

    /**
     *
     * @param dexPath dumped dex filepath;
     * @param binPaths dumped bin filepaths: this bin includes ins->codeItems;
     * @param freshOffset false -> just fix dex; true -> fix dex and read dex data;
     * @return FIX_SUCCESS or fix fail reason;
     */
    public static String fixDex(String dexPath, String[] binPaths, boolean freshOffset){
        String fixRes = "";
        fixRes = checkPath(dexPath,binPaths);
        if(!fixRes.equals(PATH_FINE)){
            return fixRes;
        }
        if(!freshOffset){
            //only fix dex
            DexFixUtils dexFixUtils = new DexFixUtils(dexPath,binPaths);
            fixRes = dexFixUtils.fixDex();
        }else {
            //fix dex + codeItem offset ;
            DexFixPlusUtils dexFixPlusUtils = new DexFixPlusUtils(dexPath,binPaths);
            dexFixPlusUtils.fixDex();

        }
        return fixRes;
    }

    private static String checkPath(String dexPath, String[] binPaths){
        if(dexPath == null || dexPath.isEmpty() ){
            return F_DEXPATH_NULL;
        }
        if(!dexPath.endsWith(DEX_END)){
            return F_DEXPATH_END;
        }
        File dexFile = new File(dexPath);
        if(!dexFile.exists()){
            return F_DEX_NOTEXISTS;
        }
        if(dexFile.length()<=0){
            return F_DEX_LENERR;
        }
        for(String binPath : binPaths){
            if(binPath == null || binPath.isEmpty()){
                return F_BINPATH_NULL;
            }
            if(!binPath.endsWith(BIN_END)){
                return F_BINPATH_END;
            }
            File binFile = new File(binPath);
            if(!binFile.exists()){
                return F_BIN_NOTEXISTS;
            }
            if(binFile.length()<=0){
                return F_BIN_LENERR;
            }
        }

        return PATH_FINE;
    }


}
