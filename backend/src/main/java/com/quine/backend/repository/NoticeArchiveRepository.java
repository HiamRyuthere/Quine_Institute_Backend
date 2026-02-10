package com.quine.backend.repository;

import com.quine.backend.model.NoticeArchive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeArchiveRepository extends JpaRepository<NoticeArchive, Long> {
}
