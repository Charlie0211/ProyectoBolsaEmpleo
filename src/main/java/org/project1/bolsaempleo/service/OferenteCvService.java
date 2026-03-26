package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Oferente;
import org.project1.bolsaempleo.entity.OferenteCv;
import org.project1.bolsaempleo.repository.OferenteCvRepository;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class OferenteCvService {

    private final OferenteCvRepository oferenteCvRepository;
    private final OferenteRepository oferenteRepository;

    @Value("${cv.upload.dir:uploads/cv}")
    private String uploadDir;

    public OferenteCvService(OferenteCvRepository oferenteCvRepository,
                             OferenteRepository oferenteRepository) {
        this.oferenteCvRepository = oferenteCvRepository;
        this.oferenteRepository = oferenteRepository;
    }

    public Optional<OferenteCv> obtenerCvDelOferente(Long oferenteId) {
        return oferenteCvRepository.findByOferenteId(oferenteId);
    }

    @Transactional
    public OferenteCv guardarCv(Long oferenteId, MultipartFile archivo) throws Exception {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar un archivo PDF.");
        }

        if (!archivo.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("El archivo debe ser un PDF válido.");
        }

        if (archivo.getSize() > 5_000_000) { // 5MB
            throw new IllegalArgumentException("El archivo no debe superar 5MB.");
        }

        Oferente oferente = oferenteRepository.findById(oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("El oferente no existe."));

        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para el archivo
        String nombreOriginal = archivo.getOriginalFilename();
        String nombreUnico = UUID.randomUUID() + "_" + nombreOriginal;
        Path rutaCompleta = uploadPath.resolve(nombreUnico);

        // Guardar archivo en el sistema de archivos
        Files.write(rutaCompleta, archivo.getBytes());

        // Buscar CV anterior y eliminarlo si existe
        Optional<OferenteCv> cvAnterior = oferenteCvRepository.findByOferenteId(oferenteId);
        if (cvAnterior.isPresent()) {
            OferenteCv cv = cvAnterior.get();
            // Intentar eliminar archivo antiguo
            try {
                Files.deleteIfExists(Paths.get(cv.getRutaArchivo()));
            } catch (Exception e) {
                // Continuar aunque falle la eliminación del archivo
            }
            oferenteCvRepository.delete(cv);
        }

        // Crear nuevo registro de CV
        OferenteCv nuevoCV = new OferenteCv(oferente, nombreOriginal, rutaCompleta.toString());
        return oferenteCvRepository.save(nuevoCV);
    }
}

