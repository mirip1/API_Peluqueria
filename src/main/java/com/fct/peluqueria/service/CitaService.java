package com.fct.peluqueria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.CitaDTO;
import com.fct.peluqueria.models.Cita;
import com.fct.peluqueria.repository.CitaRepository;
import com.fct.peluqueria.repository.UsuarioRepository;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Crea una nueva cita a partir del CitaDTO recibido.
     * Si no se especifica el estado, se asigna por defecto ACTIVA.
     *
     * @param citaDTO objeto de transferencia con la informacion necesaria.
     * @return El CitaDTO resultante tras guardar la cita.
     */
    public CitaDTO crearCita(CitaDTO citaDTO) {
      //Si ya existe un acita con las mismas características pero cancelada la eliminamos
      citaRepository
          .findByUsuarioIdAndFechaYHoraAndEstado(citaDTO.getUsuarioId(), citaDTO.getFechaYHora(), EstadoCita.CANCELADA)
          .ifPresent(citaCancelada -> {
            citaRepository.delete(citaCancelada);
          });
      Cita cita = ConverterUtil.citaDTOToCita(citaDTO, usuarioRepository);
        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCita.ACTIVA);
        }
        Cita savedCita = citaRepository.save(cita);
        return ConverterUtil.citaToCitaDTO(savedCita);
      }

    /**
     * Obtiene la lista de citas asociadas a un usuario.
     *
     * @param usuarioId Identificador del usuario.
     * @return Lista de CitaDTO correspondientes a las citas del usuario.
     */
    public List<CitaDTO> obtenerCitasPorUsuario(Integer usuarioId) {
        List<Cita> citas = citaRepository.findByUsuarioId(usuarioId);
        return citas.stream()
            .map(ConverterUtil::citaToCitaDTO)
            .collect(Collectors.toList());
      }

    /**
     * Cancela una cita dado su id.
     * Se marca la cita con el estado CANCELADA.
     *
     * @param id Identificador de la cita a cancelar.
     */
    public void cancelarCita(Integer id) {
      Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
      cita.setEstado(EstadoCita.CANCELADA);
      citaRepository.save(cita);
      }
    }
