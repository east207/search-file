package com.gzh.pojo.Tika;


import lombok.Data;

/**
 * tika解析出的对象，一个文件对应一个对象
 *
 */
@Data
public class TikaParaseObject {


    public int id;

    public String fileName;

    public String fileContent;


}
