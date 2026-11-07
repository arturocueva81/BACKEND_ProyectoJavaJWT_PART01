package com.krakedev.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.jwt.entidades.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Integer>{

}
