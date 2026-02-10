package com.quine.backend.service;


import com.quine.backend.model.Notice;
import com.quine.backend.model.NoticeArchive;
import com.quine.backend.repository.NoticeArchiveRepository;
import com.quine.backend.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeArchiveRepository noticeArchiveRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    public Notice postNotice(Notice notice ){

        notice.setDate(LocalDate.now());
        //SETTING THE DATE OF NOTICE FOR TODAY
        if(notice.getDateOfExecution() == null ){
            notice.setDateOfExecution(LocalDate.now().plusDays(7));
        }

        notice.setNew(true);
        return noticeRepository.save(notice);
    }

    // 2. Notices lane ka method (With AUTO-ARCHIVE Logic 🪄)
    public List<Notice> getAllNotices() {
        List<Notice> allNotices = noticeRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Notice notice : allNotices) {

            // Logic A: Move to Archive if (Today > ExecutionDate + 1)
            // Example: Event 25 ko tha. 25+1=26. Agar aaj 27 hai, to archive karo.
            if (today.isAfter(notice.getDateOfExecution().plusDays(1))) {
                moveToArchive(notice);
            }
            // Logic B: Remove 'New' tag if (Today > IssueDate + 1)
            else if (notice.isNew() && today.isAfter(notice.getDate().plusDays(1))) {
                notice.setNew(false);
                noticeRepository.save(notice); // Update DB
            }
        }

        // Filter karke wapas bhejenge (Jo archive ho gaye wo list mein nahi aayenge)
        return noticeRepository.findAll();
    }

    //Helper method : Notice - > Archive

    private void moveToArchive(Notice notice ){
        NoticeArchive archive = new NoticeArchive();
        archive.setTitle(notice.getTitle());
        archive.setContent(notice.getContent());
        archive.setDate(notice.getDate());
        archive.setType(notice.getType());
        archive.setDate(notice.getDate());
        archive.setDateOfExecution(notice.getDateOfExecution());

        noticeArchiveRepository.save(archive);
        //saving in archive

        noticeRepository.delete(notice);

        System.out.println("Notice archived"+notice.getTitle());
    }

    public List<NoticeArchive> getAllArchive(){
        return noticeArchiveRepository.findAll();
    }

}
