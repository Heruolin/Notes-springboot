package com.sca.notesspringboot.web.controller;

import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.entity.Notes;
import com.sca.notesspringboot.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.Collections;

import java.util.List;

@RestController
@RequestMapping("Notes")
public class NotesController {

    @Autowired
    NotesService notesService;

    // 查询所有便签
    @GetMapping("/Noteslist")
    public Result selectNotes() {
        List<Notes> list = notesService.selectNotes(); // 正确调用 service 方法
        return Result.success(list);
    }

    // 根据关键字查询便签
    @GetMapping("/NotesSearch")
    public Result selectNotesByKeyword(@RequestParam String keyword) {
        List<Notes> list = notesService.selectNotesByKeyword(keyword);
        return Result.success(list);
    }

    // 根据标签查询便签
    @GetMapping("/NotesByTag")
    public Result selectNotesByTag(@RequestParam String tag) {
        List<Notes> list = notesService.selectNotesByTag(tag);
        return Result.success(list);
    }

    // 添加便签
    @PostMapping("/NotesAdd")
    public Result insertNotes(@RequestBody Notes notes) {
        notesService.insertNotes(notes);
        return Result.success("便签添加成功");
    }

    // 更新便签
    @PutMapping("/NotesUpdate")
    public Result updateNotes(@RequestBody Notes notes) {
        notesService.updateNotes(notes);
        return Result.success("便签更新成功");
    }

    // 更新便签顺序
    @PutMapping("/UpdateOrder")
    public Result updateOrder(@RequestBody List<Notes> notesList) {
        try {
            for (Notes note : notesList) {
                notesService.updateOrder(note.getId(), note.getOrder());
            }
            return Result.success("顺序更新成功");
        } catch (Exception e) {
            return Result.error("顺序更新失败: " + e.getMessage());
        }
    }

    // 删除便签
    @DeleteMapping("/NotesSelete")
    public Result deleteNotes(@RequestBody Notes notes) {
        notesService.deleteNotes(notes);
        return Result.success("便签删除成功");
    }

    // 上传图片
    @Value("${upload.dir}")
    private String uploadDir;

    // 上传图片
    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("id") int id) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 获取原文件名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = null;
        if (originalFileName != null) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // 生成唯一的文件名（防止重复）
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 创建目标文件路径
        File dest = new File(uploadDir + uniqueFileName);

        // 确保文件夹存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            // 将文件保存到目标位置
            file.transferTo(dest);

            // 获取当前便签的所有图片（如果有的话）
            Notes notes = notesService.selectById(id); // 根据 ID 查找便签

            if (notes == null) {
                return Result.error("找不到该便签");
            }

            String currentImages = notes.getImg(); // 获取数据库中已有的图片字段

            // 判断如果当前图片字段为空，则不加逗号
            String newImages = currentImages == null || currentImages.isEmpty() ? uniqueFileName : currentImages + "," + uniqueFileName;

            // 更新数据库中的图片字段
            notesService.updateImage(id, newImages);  // 更新数据库中的 img 字段

            // 返回包含 fileName 属性的对象
            return Result.success("文件上传成功").data(Collections.singletonMap("fileName", uniqueFileName));
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    // 删除图片
    @PostMapping("/deleteImage")
    public Result deleteImage(@RequestParam("id") int id, @RequestParam("imageName") String imageName) {
        try {
            Notes notes = notesService.selectById(id);

            if (notes == null) {
                return Result.error("找不到该便签");
            }

            String currentImages = notes.getImg(); // 获取数据库中的图片字段
            if (currentImages != null) {
                // 将要删除的图片从字段中移除
                String updatedImages = currentImages.replace(imageName, "").replace(",,", ","); // 删除图片并修正多余的逗号

                // 如果删除后没有图片，设置为空
                if (updatedImages.isEmpty() || updatedImages.equals(",")) {
                    updatedImages = null;
                }

                // 更新数据库中的 img 字段
                notesService.updateImage(id, updatedImages);  // 更新数据库中的 img 字段

                // 删除服务器中的文件
                File fileToDelete = new File(uploadDir + imageName);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }

                return Result.success("图片删除成功");
            } else {
                return Result.error("该便签没有图片");
            }

        } catch (Exception e) {
            return Result.error("删除图片失败: " + e.getMessage());
        }
    }

    // 归档便签
    @PutMapping("/Archive/Add")
    public Result archiveNotes(@RequestParam int id) {
        try {
            notesService.archiveNotes(id);
            return Result.success("便签归档成功");
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return Result.error("便签归档失败: " + e.getMessage());
        }
    }

    //删除便签
    @PutMapping("/Trash/Add")
    public Result trashNotes(@RequestParam int id) {
        try {
            notesService.trashNotes(id);
            return Result.success("便签删除成功");
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return Result.error("便签删除失败: " + e.getMessage());
        }
    }

    // 还原归档便签
    @PutMapping("/Archive/Restore")
    public Result restoreArchivedNote(@RequestParam int id) {
        try {
            notesService.restoreArchivedNote(id);
            return Result.success("便签还原成功");
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return Result.error("便签还原失败: " + e.getMessage());
        }
    }

    //还原删除便签
    @PutMapping("/Trash/Restore")
    public Result restoreTrashNote(@RequestParam int id) {
        try {
            notesService.restoreTrashNote(id);
            return Result.success("便签还原成功");
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return Result.error("便签还原失败: " + e.getMessage());
        }
    }

    // 查询归档内所有便签
    @GetMapping("/Archive/Noteslist")
    public Result selectArchiveNotes() {
        List<Notes> list = notesService.selectArchiveNotes();
        return Result.success(list);
    }

    // 查询回收站内所有便签
    @GetMapping("/Trash/Noteslist")
    public Result selectTrashNotes() {
        List<Notes> list = notesService.selectTrashNotes();
        return Result.success(list);
    }

    // 彻底删除便签
    @DeleteMapping("/Trash/Delete")
    public Result deleteTrashNote(@RequestParam int id) {
        try {
            notesService.deleteTrashNote(id);
            return Result.success("便签彻底删除成功");
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return Result.error("便签彻底删除失败: " + e.getMessage());
        }
    }

}

