package com.activachilecito.adapter.complejo.crud;

import com.activachilecito.adapter.complejo.entity.data.ComplejoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplejoCrud extends JpaRepository<ComplejoData, Long> {

}
