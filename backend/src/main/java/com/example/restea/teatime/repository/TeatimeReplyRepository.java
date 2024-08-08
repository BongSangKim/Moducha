package com.example.restea.teatime.repository;

import com.example.restea.teatime.entity.TeatimeComment;
import com.example.restea.teatime.entity.TeatimeReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeatimeReplyRepository extends JpaRepository<TeatimeReply, Integer> {

    Long countByTeatimeComment(TeatimeComment teatimeComment);
}
