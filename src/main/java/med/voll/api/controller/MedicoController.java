package med.voll.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    @Operation(summary = "Registrar un nuevo médico",
            description = "Crea un nuevo registro de médico en la base de datos.")
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder){
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        //Return 201 created
        //URL donde encontrar al medico
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(),
                medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
//        URI url = "http://localhost:8080/medicos/" + medico.getId();
        URI url = uriComponentsBuilder.path("medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);

    }

    @GetMapping
    @Operation(summary = "Obtener todos los médicos activos",
            description = "Retorna una lista con todos los médicos que tienen el atributo 'activo' como verdadero.")
    public ResponseEntity<Page<DatosListadoMedico>>  listadoMedicos(@PageableDefault(size=10) Pageable paginacion) {
//        return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }

    @PutMapping
    @Operation(summary = "Actualizar los datos de un médico",
            description = "Modifica los datos de un médico existente en la base de datos.")
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(),
                medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento())));
    }

// DELETE LOGICO
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un médico",
            description = "Marca un médico como inactivo en la base de datos.")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id)  {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

//DELETE EN BASE DE DATOS
    //    public void eliminarMedico(@PathVariable Long id)  {
//        Medico medico = medicoRepository.getReferenceById(id);
//        medicoRepository.delete(medico);
//    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un médico por ID",
            description = "Retorna los datos de un médico específico a partir de su ID.")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedicos(@PathVariable Long id)  {
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(),
                medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }

}
