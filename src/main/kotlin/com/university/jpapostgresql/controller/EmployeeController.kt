package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Employee
import com.university.jpapostgresql.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EmployeeController {

    @Autowired
    lateinit var repository: EmployeeRepository

    @RequestMapping("/save")
    fun save(): String {
        repository.save(Employee("Канарейка", "Иван", "г.Донецк, ул. Артёма, 25/5", "0713333322"))
		repository.save(Employee("Горбань", "Елена", "г.Донецк, ул. Ленина, 47/2", "0714444455"))
		repository.save(Employee("Умко", "Мария", "г.Макеевка, ул. Бирюзова, 5/15", "0712222299"))
		repository.save(Employee("Борбуч", "Виталий", "г.Донецк, ул. Куприна, 21", "0718889977"))
		repository.save(Employee("Цветок", "Илья", "г.Донецк, ул. Ткаченко, 53/15", "0711114455"))

		return "Done"
	}

    @RequestMapping("/findall")
    fun findAll() = repository.findAll()

    @RequestMapping("/findbyid/{id}")
    fun findById(@PathVariable id: Long)
            = repository.findById(id)

    @RequestMapping("/findbysurname/{surname}")
    fun findByLastName(@PathVariable surname: String)
            = repository.findBySurname(surname)
}