package com.example.blog2.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.PicturesEntity;
import com.example.blog2.service.PicturesService;
import org.springframework.web.multipart.MultipartFile;



/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/pictures")
public class PicturesController {

    @Autowired
    private PicturesService picturesService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R AllList(@RequestParam Map<String, Object> params){
        PageUtils page = picturesService.queryPage(params, true);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PicturesEntity pictures = picturesService.getById(id);

        return R.ok().put("pictures", pictures);
    }

    /**
     * 保存
     */
    @PostMapping("/saveImg")
    public R save(@RequestBody PicturesEntity pictures){
        pictures.setBelong(-1L);
        picturesService.save(pictures);
        return R.ok().put("data", pictures.getId());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PicturesEntity pictures){
		picturesService.updateById(pictures);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody PicturesEntity pictures){
        picturesService.delPic(pictures);
        return R.ok();
    }

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        String img = picturesService.upload(file);
        return R.ok().put("data", img);
    }

    @PostMapping("/updateShowStatus")
    public R updateShowStatus(@RequestBody PicturesEntity pictures){
        picturesService.updateShowStatus(pictures);
        return R.ok();
    }
}
