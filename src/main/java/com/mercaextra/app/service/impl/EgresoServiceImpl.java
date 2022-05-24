package com.mercaextra.app.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mercaextra.app.config.Utilities;
import com.mercaextra.app.domain.Egreso;
import com.mercaextra.app.repository.EgresoRepository;
import com.mercaextra.app.service.EgresoService;
import com.mercaextra.app.service.dto.EgresoDTO;
import com.mercaextra.app.service.mapper.EgresoMapper;

/**
 * Service Implementation for managing {@link Egreso}.
 */
@Service
@Transactional
public class EgresoServiceImpl implements EgresoService {

    private final Logger log = LoggerFactory.getLogger(EgresoServiceImpl.class);

    private final EgresoRepository egresoRepository;

    private final EgresoMapper egresoMapper;

    public EgresoServiceImpl(EgresoRepository egresoRepository, EgresoMapper egresoMapper) {
        this.egresoRepository = egresoRepository;
        this.egresoMapper = egresoMapper;
    }

    @Override
    public EgresoDTO save(EgresoDTO egresoDTO) {
        log.debug("Request to save Egreso : {}", egresoDTO);
        Egreso egreso = egresoMapper.toEntity(egresoDTO);
        egreso = egresoRepository.save(egreso);
        return egresoMapper.toDto(egreso);
    }

    @Override
    public Optional<EgresoDTO> partialUpdate(EgresoDTO egresoDTO) {
        log.debug("Request to partially update Egreso : {}", egresoDTO);

        return egresoRepository
            .findById(egresoDTO.getId())
            .map(existingEgreso -> {
                egresoMapper.partialUpdate(existingEgreso, egresoDTO);

                return existingEgreso;
            })
            .map(egresoRepository::save)
            .map(egresoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgresoDTO> findAll() {
        log.debug("Request to get all Egresos");
        return egresoRepository.findAll().stream().map(egresoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EgresoDTO> findOne(Long id) {
        log.debug("Request to get Egreso : {}", id);
        return egresoRepository.findById(id).map(egresoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egreso : {}", id);
        egresoRepository.deleteById(id);
    }

    
	/*
	 * METODO PARA CONSULTAR EL VALOR DE LOS EGRESOS DEL DIA, SE IMPLEMENTA UNA
	 * LOGICA PARA RESTAR UN DIA EN CASO DE QUE SE HAGA UNA PETICION DESPUES DE LAS
	 * 19:00 YA QUE EL TIPO DE DATO INSTANT DESPUES DE LAS 19 HORAS POR CUESTION DE
	 * MINUTOS Y HORAS LOCALES AUMENTA 1 DIA.
	 */
	@Override
	public BigDecimal valueDayleEgress() {
		log.debug("Request to get value egress day");
	
		String instantFormat = Utilities.validateDate(Instant.now());
		
		BigDecimal valueDayEgress = egresoRepository.valueDailyEgreso(instantFormat);
		
		BigDecimal valueResponse = valueDayEgress == null ?  BigDecimal.ZERO : valueDayEgress;
		
		return valueResponse;
	}
	
	
	/*
	 * private String validateHour(Instant fecha) {
	 * log.debug("Request to validate hour instant");
	 * 
	 * Instant instant = Instant.now(); String instantFormat;
	 * 
	 * LocalTime localTime = LocalTime.now(); String localFormat =
	 * localTime.toString().substring(0, 2);
	 * 
	 * int timeInt = Integer.parseInt(localFormat);
	 * 
	 * if(timeInt >= 19) { Calendar calendar = Calendar.getInstance();
	 * 
	 * calendar.add(Calendar.DAY_OF_MONTH, -1);
	 * 
	 * Instant daySubtract = calendar.toInstant(); instantFormat =
	 * daySubtract.toString().substring(0, 10); }else { instantFormat =
	 * instant.toString().substring(0, 10); }
	 * 
	 * return instantFormat; }
	 */

	@Override
	public List<EgresoDTO> dayleEgress() {
		log.debug("Request to get all daily egress");
		String dateFormat = Utilities.validateDate(Instant.now());
		return egresoRepository.dailyEgress(dateFormat)
				.stream()
				.map(egresoMapper :: toDto)
				.collect(Collectors.toCollection(LinkedList :: new));
	}
}
