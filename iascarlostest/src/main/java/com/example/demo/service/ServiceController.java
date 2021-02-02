package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

	@Autowired
	private ServiceRepository repository;

	HoursModel hm = new HoursModel(0, 0, 0, 0, 0, 0, 0);

	@GetMapping("/service")
	public List<ServiceModel> getServices() {
		return repository.findAll();
	}

	@GetMapping("/findService/{technicialId}/{weekNumber}")
	public HoursModel getService(@PathVariable String technicialId, @PathVariable int weekNumber) {

		resetHours();
		List<ServiceModel> resList = repository.findBytechnicialId(technicialId);

		for (ServiceModel service : resList) {
			if (searchWeek(service) == weekNumber) {
				System.out.println("Entro aquí");
				calculateWork(service);
			}
		}
		return hm;

	}

	@PostMapping("/addService")
	public String addService(@RequestBody ServiceModel service) {
		if (service.endDateTime.after(service.startDateTime)) {
			repository.save(service);
			return "Se añadió el servicio con id : " + service.id;
		}
		return "No se pudo añadir el servicio, revise la información suministrada";
	}

	@DeleteMapping("/deleteService/{id}")
	public String deleteService(@PathVariable String id) {
		repository.deleteById(id);
		return "Service deleted with id : " + id;
	}

	public HoursModel calculateWork(ServiceModel sm) {
		Calendar calendarStart = Calendar.getInstance();
		Calendar calendarEnd = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
		calendarStart.setTime(sm.startDateTime);
		calendarEnd.setTime(sm.endDateTime);
		calendarStart.add(Calendar.HOUR_OF_DAY, 5);
		calendarEnd.add(Calendar.HOUR_OF_DAY, 5);
		
		int startDay = calendarStart.get(Calendar.DAY_OF_WEEK);
		int endDay = calendarEnd.get(Calendar.DAY_OF_WEEK);
		int startHour = calendarStart.get(Calendar.HOUR_OF_DAY);
		int endHour = calendarEnd.get(Calendar.HOUR_OF_DAY);

		int totalHours = 0;
		long normalHours = 0;
		long nightHours = 0;
		long extraNormalHours = 0;
		long extraNightHours = 0;
		long dominicalHours = 0;
		long extraDominicalHours = 0;
		
		System.out.println(startDay);
		System.out.println(endDay);
		System.out.println(startHour);
		System.out.println(endHour);


		if (totalHours > 48) {
			if (startDay == 1 && endDay == 1) { // Dominicales extra
				extraDominicalHours += calculateHours(sm.startDateTime, sm.endDateTime);
				totalHours += extraDominicalHours;
			} else if (startDay == 7 && endDay == 1) {
				if (startHour < 20) {
					long normalBeforestartHour = startHour - 20;
					extraNightHours = 24 - startHour + normalBeforestartHour;
					extraNormalHours = normalBeforestartHour;
					extraDominicalHours = 0 + endHour;
					totalHours += extraNightHours + extraDominicalHours + extraNormalHours;
				} else {
					extraNightHours = 24 - startHour;
					extraDominicalHours = 0 + endHour;
					totalHours += extraNightHours + extraDominicalHours;
				}
			} else {
				if (startHour >= 7 && startHour <= 20 && endHour >= 7 && endHour <= 20) { // diurna extra
					extraNormalHours += calculateHours(sm.startDateTime, sm.endDateTime);
					totalHours += extraNormalHours;
				}
				if (startHour >= 20 && startHour <= 23
						|| startHour >= 0 && startHour < 7 && endHour > 20 && endHour <= 23 // nocturna extra
						|| endHour >= 0 && endHour < 7) {
					extraNightHours += calculateHours(sm.startDateTime, sm.endDateTime);
					totalHours += extraNightHours;
				}
			}
		} else {
			if (startDay == 1 && endDay == 1) {
				System.out.println("Domingo");
				dominicalHours += calculateHours(sm.startDateTime, sm.endDateTime);
				totalHours += dominicalHours;
			} else if (startDay == 7 && endDay == 1) {
				System.out.println("Sabado - Domingo");
				if (startHour < 20) {
					long normalBeforestartHour = startHour - 20;
					nightHours = 24 - startHour + normalBeforestartHour;
					normalHours = normalBeforestartHour;
					dominicalHours = 0 + endHour;
					totalHours += nightHours + dominicalHours + normalHours;
				} else {
					nightHours = 24 - startHour;
					dominicalHours = 0 + endHour;
					totalHours += nightHours + dominicalHours;
				}
			} else {
				System.out.println("Otro");
				if (startHour >= 7 && startHour <= 20 && endHour >= 7 && endHour <= 20) {
					normalHours += calculateHours(sm.startDateTime, sm.endDateTime);
					totalHours += normalHours;
				}
				if (startHour >= 20 && startHour <= 23
						|| startHour >= 0 && startHour < 7 && endHour > 20 && endHour <= 23
						|| endHour >= 0 && endHour < 7) {
					System.out.println("Sabado Noche");
					nightHours += calculateHours(sm.startDateTime, sm.endDateTime);
					totalHours += nightHours;
				}
			}
		}

		totalHours += hm.totalHours;
		normalHours += hm.normalHours;
		nightHours += hm.nightHours;
		extraNormalHours += hm.normalExtraHours;
		extraNightHours += hm.nightExtraHours;
		dominicalHours += hm.dominicalHours;
		extraDominicalHours += hm.dominicalExtraHours;

		hm = new HoursModel(totalHours, normalHours, nightHours, extraNormalHours, extraNightHours, dominicalHours,
				extraDominicalHours);

		return hm;

	}

	public long calculateHours(Date start, Date end) {
		long diff = end.getTime() - start.getTime();
		return diff / (60 * 60 * 1000);
	}

	public int searchWeek(ServiceModel service) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
		calendar.setTime(service.startDateTime);
		calendar.add(Calendar.HOUR_OF_DAY, 5);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	private void resetHours() {
		hm.totalHours = 0;
		hm.normalHours = 0;
		hm.nightHours = 0;
		hm.normalExtraHours = 0;
		hm.nightExtraHours = 0;
		hm.dominicalHours = 0;
		hm.dominicalExtraHours = 0;
	}

}
