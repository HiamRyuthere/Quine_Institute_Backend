package com.quine.backend.controller;

import com.quine.backend.model.Notice;
import com.quine.backend.model.NoticeArchive;
import com.quine.backend.service.NoticeService;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notices")
@CrossOrigin(origins = "*")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping
    public Notice addNotice( @RequestBody Notice notice ){
        return noticeService.postNotice(notice);
    }

    @GetMapping
    public List<Notice> getAllNotice (){
        return noticeService.getAllNotices();
    }

    @GetMapping("/archive")
    public List<NoticeArchive> getAllArchiveNotice(){
        return noticeService.getAllArchive();
    }

}
