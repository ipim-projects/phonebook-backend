package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ipim.phonebook.model.*;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.createdAt >= CURRENT_DATE - 30")
    long countForMonth();

    // 1. Поиск сотрудников одной и той же организации с абонентом
    @Query("SELECT e FROM Employee e WHERE e.id <> ?1" +
            "AND e.job.company = (SELECT e1.job.company FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getCompanyCoworkers(Long employeeId);

    // 2. Поиск сотрудников из разных организаций, но с такой же должностью как у абонента
    @Query("SELECT e FROM Employee e WHERE e.id <> ?1" +
            "AND e.job.jobTitle = (SELECT e1.job.jobTitle FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getJobTitleCoworkers(Long employeeId);

    // 3. Поиск сотрудников из разных организаций, но работающих по одному и тому же адресу с абонентом
    @Query("SELECT e FROM Employee e WHERE e.id <> ?1" +
            "AND e.job.address = (SELECT e1.job.address FROM Employee e1 WHERE e1.id = ?1)")
    List<Employee> getCompanyAddressCoworkers(Long employeeId);


    // Выгрузка тип 2 : сотрудники с привязкой к местам работы, сортировка по Employee.id
    // Поля: фамилия,имя, мобильный телефон, email +  Наименование организации, Должность, Рабочий адрес.
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType2(e.firstName, e.lastName, e.mobilePhone, e.email, j.company, j.jobTitle, j.address) FROM Employee AS e LEFT JOIN Job As j ON j.id = e.job.id ORDER BY e.id" )
    List<EmpExportType2> exportType2WithJPQL();
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType2(e.firstName, e.lastName, e.mobilePhone, e.email, j.company, j.jobTitle, j.address) FROM Employee AS e LEFT JOIN Job As j ON j.id = e.job.id Where e.createdAt between to_date(:d1,'YYYY-MM-DD') and to_date(:d2,'YYYY-MM-DD') ORDER BY e.id" )
    List<EmpExportType2> exportType2dWithJPQL(String d1, String d2);

    // Выгрузка тип 1 : Телефонная книга с сортировкой по фамилии и имени
    @Query("SELECT new ru.ipim.phonebook.model.EmpExportType1(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email) FROM Employee AS e ORDER BY e.lastName, e.firstName")
    List<EmpExportType1> exportType1WithJPQL();
    @Query("SELECT new ru.ipim.phonebook.model.EmpExportType1(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email) FROM Employee AS e Where e.createdAt between to_date(:d1,'YYYY-MM-DD') and to_date(:d2,'YYYY-MM-DD') ORDER BY e.lastName, e.firstName")
    List<EmpExportType1> exportType1dWithJPQL(String d1, String d2);

    @Query(value = "SELECT new ru.ipim.phonebook.model.StatCompanys( j.company AS company, COUNT(e.job.id) AS cnt) FROM Employee AS e JOIN Job As j ON j.id = e.job.id  GROUP BY j.company ORDER BY cnt DESC, company")
    List<StatCompanys> findGroupByCompanysWithJPQL();

}
