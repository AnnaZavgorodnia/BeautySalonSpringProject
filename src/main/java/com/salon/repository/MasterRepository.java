package com.salon.repository;

import com.salon.entity.Master;
import com.salon.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
}
