package com.krakedev.jwt.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.krakedev.jwt.entidades.Mascota;
import com.krakedev.jwt.repositories.MascotaRepository;

@RestController
@RequestMapping("/api/auth/mascotas")
public class MascotaController {

	private final MascotaRepository mascotaRepository;

	public MascotaController(MascotaRepository mascotaRepository) {
		this.mascotaRepository = mascotaRepository;
	}

	@PostMapping
	public ResponseEntity<?> crear(@RequestParam("nombre") String nombre, @RequestParam("raza") String raza,
			@RequestParam("foto") MultipartFile foto) {
		try {
			if (foto.isEmpty()) {
				return ResponseEntity.badRequest().body("Debe seleccionar una foto");
			}
			Mascota mascota = new Mascota();
			mascota.setNombre(nombre);
			mascota.setRaza(raza);
			mascota.setMimeType(foto.getContentType());
			mascota.setFoto(foto.getBytes());
			Mascota guardada = mascotaRepository.save(mascota);
			return ResponseEntity.status(201).body(guardada);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error al crear la mascota: " + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> listar() {
		try {
			List<Mascota> mascotas = mascotaRepository.findAll();
			return ResponseEntity.ok(mascotas);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error al listar las mascotas: " + e.getMessage());
		}
	}

	@GetMapping("/{id}/foto")
	public ResponseEntity<?> obtenerFoto(@PathVariable Integer id) {
		try {
			Optional<Mascota> optional = mascotaRepository.findById(id);
			if (optional.isPresent()) {
				Mascota mascota = optional.get();
				if (mascota.getFoto() == null) {
					return ResponseEntity.notFound().build();
				}
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(mascota.getMimeType()))
						.body(mascota.getFoto());
			}
			return ResponseEntity.status(404).body("Mascota no encontrada");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error al obtener la foto: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable Integer id, @RequestParam("nombre") String nombre,
			@RequestParam("raza") String raza, @RequestParam(value = "foto", required = false) MultipartFile foto) {
		try {
			Optional<Mascota> optional = mascotaRepository.findById(id);
			if (optional.isPresent()) {
				Mascota mascota = optional.get();
				mascota.setNombre(nombre);
				mascota.setRaza(raza);
				if (foto != null) {
					if (!foto.isEmpty()) {
						mascota.setMimeType(foto.getContentType());
						mascota.setFoto(foto.getBytes());
					}
				}
				Mascota actualizada = mascotaRepository.save(mascota);
				return ResponseEntity.ok(actualizada);
			}
			return ResponseEntity.status(404).body("Mascota no encontrada");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error al editar la mascota: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		try {
			Optional<Mascota> optional = mascotaRepository.findById(id);
			if (optional.isPresent()) {
				mascotaRepository.deleteById(id);
				return ResponseEntity.ok("Mascota eliminada correctamente");
			}
			return ResponseEntity.status(404).body("Mascota no encontrada");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error al eliminar la mascota: " + e.getMessage());
		}
	}
}