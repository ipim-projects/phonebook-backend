package ru.ipim.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.ipim.phonebook.model.Employee;
import ru.ipim.phonebook.model.EmpExportType0;
import ru.ipim.phonebook.model.EmpExportType1;
import ru.ipim.phonebook.model.EmpExportType2;
import ru.ipim.phonebook.model.StatCompanys;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.createdAt >= CURRENT_DATE - 30")
    long countForMonth();

    // Выгрузка тип 0 : Телефонная книга с сортировкой по фамилии и имени + поля из таблицы job по связке employee.job_id = job.id
    // поля : Фамилия, Имя, Рабочий телефон, Мобильный телефон, e-mail, Дата рождения +  Наименование организации, Должность, Рабочий адрес.
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType0(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email, j.company, j.jobTitle, j.address) FROM Employee AS e JOIN Job As j ON j.id = e.jobId ORDER BY e.lastName, e.firstName")
    List<EmpExportType0> exportAllWithJPQL();

    // Выгрузка тип 1 : Телефонная книга с сортировкой по фамилии и имени
    // поля : Фамилия, Имя, Рабочий телефон, Мобильный телефон, e-mail, Дата рождения
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType1(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email) FROM Employee AS e ORDER BY e.lastName, e.firstName")
    List<EmpExportType1> exportType1WithJPQL();

    // Выгрузка тип 2 : сотрудники с привязкой к местам работы, сортировка по Employee.id
    // Поля: фамилия,имя, мобильный телефон, email +  Наименование организации, Должность, Рабочий адрес.
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType2(e.firstName, e.lastName, e.mobilePhone, e.email, j.company, j.jobTitle, j.address) FROM Employee AS e LEFT JOIN Job As j ON j.id = e.jobId ORDER BY e.id" )
    List<EmpExportType2> exportType2WithJPQL();
    //@Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType2(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email, j.company, j.jobTitle, j.address) FROM Job AS j, Employee AS e Where (e.jobId is not null) and e.jobId=j.id ORDER BY e.id")
    //List<EmpExportType2> exportType2WithJPQL();

    @Query(value = "SELECT new ru.ipim.phonebook.model.StatCompanys( j.company AS company, COUNT(e.jobId) AS cnt) FROM Employee AS e JOIN Job As j ON j.id = e.jobId  GROUP BY j.company ORDER BY cnt DESC, company")
    List<StatCompanys> findGroupByCompanysWithJPQL();


    // Во всех трех поисках из результата поиска исключается сам абонент n

    //coworkers-company
    // 1. Поиск сотрудников одной и той же организации с абонентом n
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType1(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email) FROM Employee AS e, Job As j  Where (j.id = e.jobId) and (e.jobId is not null) and (e.id <> :n) and j.company = (SELECT DISTINCT jj.company from Job jj, Employee AS ee where jj.id=ee.jobId and ee.id = :n) ORDER BY e.lastName, e.firstName")
    List<EmpExportType1> findCoworkersCompanyWithJPQL(long n);

    //coworkers-job
    // 2. Поиск сотрудников из разных организаций, но с такой же должностью как у абонента n
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType1(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email) FROM Employee AS e JOIN Job As j ON j.id = e.jobId Where (e.jobId is not null) and (e.id <> :n) and j.jobTitle = (SELECT DISTINCT jj.jobTitle from Job jj, Employee AS ee where jj.id=ee.jobId and ee.id = :n) ORDER BY e.lastName, e.firstName")
    List<EmpExportType1> findCoworkersJobTitleWithJPQL(long n);

    //coworkers-address
    // 3. Поиск сотрудников из разных организаций, но работающих по одному и тому же адресу с абонентом n
    @Query(value = "SELECT new ru.ipim.phonebook.model.EmpExportType1(e.firstName, e.lastName, e.birthdate, e.workPhone, e.mobilePhone, e.email) FROM Employee AS e JOIN Job As j ON j.id = e.jobId Where (e.jobId is not null) and (e.id <> :n) and j.address = (SELECT DISTINCT jj.address from Job jj, Employee AS ee where jj.id=ee.jobId and ee.id = :n) ORDER BY e.lastName, e.firstName")
    List<EmpExportType1> findCoworkersAddressWithJPQL(long n);


}
