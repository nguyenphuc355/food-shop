package shopfoodbe.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopfoodbe.dto.ICustomerDto;
import shopfoodbe.model.Customer;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "select customer.name as name,customer.day_of_birth as dayOfBirth ,customer.gender as gender," +
            "customer.id_card as idCard,customer.email as email,customer.address as address,customer.phone_number as phoneNumber," +
            "user.username as username,user.`password` as password,customer.customer_type_id as customerTypeId  " +
            "from customer " +
            "join user on customer.username = user.username " +
            " where user.username like %:username% and customer.is_delete=0",
            countQuery = " select  count(*)",
            nativeQuery = true)
    Optional<ICustomerDto> findCustomerByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "call sign_up(:#{#c.user.username},:#{#c.user.password},:#{#c.name} ,:#{#c.dayOfBirth},:#{#c.gender},:#{#c.idCard}," +
            ":#{#c.email},:#{#c.address},:#{#c.phoneNumber},:#{#c.customerType.id})", nativeQuery = true)
    void saveCustomer(@Param("c") Customer customer);

    @Query(value = "select user.username as customerUserName,user.password as customerPassword  " +
            "from customer " +
            "join user on customer.username = user.username " +
            " where user.username like %:username% and customer.is_delete=0",
            countQuery = " select  count(*)",
            nativeQuery = true)
    Optional<ICustomerDto> findUserByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = " update customer set " +
            " name =:#{#c.name}, day_of_birth=:#{#c.dayOfBirth},gender=:#{#c.gender},id_card=:#{#c.idCard}," +
            "email=:#{#c.email},address=:#{#c.address},  phone_number =:#{#c.phoneNumber}" +
            " where username =:username", nativeQuery = true)
    void updateCustomer(@Param("c") Customer customer, @Param("username") String username);


    @Query(value = "select id, name, is_delete, day_of_birth, username, gender, id_card, email, address, phone_number, customer_type_id" +
            " from customer " +
            "where name like %:nameSearch% and address like %:addressSearch% " +
            "and phone_number like %:phoneSearch% and is_delete = 0 ",
            countQuery = "select count(*) from customer  " +
                    "where name like %:nameSearch% and address like %:addressSearch% " +
                    "and phone_number like %:phoneSearch% and is_delete = 0 ",
            nativeQuery = true)
    Page<Customer> searchCustomer(@Param("nameSearch") String nameSearch, @Param("addressSearch") String addressSearch,
                                  @Param("phoneSearch") String phoneSearch, Pageable pageable);

    @Modifying
    @Query(value = "update user set password = :password where username = :username and is_delete =0", nativeQuery = true)
    void setPassword(@Param("username") String username, @Param("password") String password);


    @Query(value = "select id, name, is_delete, day_of_birth, username, gender, id_card, email, address, phone_number, customer_type_id" +
            " from customer  where id =:id and is_delete = 0", nativeQuery = true)
    Optional<Customer> findByIdCustomer(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "update user set password =:newPassword where username =:username", nativeQuery = true)
    void saveNewPassword(@Param("newPassword") String newPassword, @Param("username") String username);

    /*
    Đạt phạm
     */
    @Query(value = "select * from customer where email = :email and is_delete = 0 LIMIT 0, 1;", nativeQuery = true)
    Customer findFakeMail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "insert into customer(name,email,username) values (:name, :email, :email)", nativeQuery = true)
    int saveCreateGmail(@Param("name") String name, @Param("email") String email);

    @Query(value = "select customer.id from customer", nativeQuery = true)
    List<Integer> findAllCustomerIdById();

    @Query(value = "select * from customer where username = :username and is_delete = 0;", nativeQuery = true)
    Customer findByUsername(@Param("username") String username);
}