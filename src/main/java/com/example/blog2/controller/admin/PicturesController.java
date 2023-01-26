package com.example.blog2.controller.admin;

import com.example.blog2.po.Pictures;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.PicturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/24 19:27
 */
@RestController
public class PicturesController {

    @Autowired
    private PicturesService picturesService;

    //    新增或删除type
    @PostMapping("/saveImg")
    public Result post(@RequestBody Map<String, Pictures> para) {
        Pictures pictures = para.get("img");
        Pictures savePicture = picturesService.saveType(pictures);
        return new Result(true, StatusCode.OK, "添加成功", savePicture.getId());
    }


    @PostMapping("/img/delete")
    public Result delete(@RequestBody Map<String, String> para) {
        Long id = Long.parseLong(para.get("id"));
        String key = para.get("key");
        picturesService.deleteType(id, key);
        return new Result(true, StatusCode.OK, "删除成功", null);
    }

    @GetMapping("/getAllImg")
    public Result getFullTypeList() {
        return new Result(true, StatusCode.OK, "获取博客全部分类成功",picturesService.listType());
    }
}
