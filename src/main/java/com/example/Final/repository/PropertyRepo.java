package com.example.Final.repository;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepo extends JpaRepository<Properties, Integer> {
    List<Properties> getPropertiesByHistoryListing(HistoryListing historyListing);

    List<Properties> getPropertiesByUser(User user);

    List<Properties> getPropertiesByOrderByPropertyPriceDescPropertyPriorityAsc();

    List<Properties> getPropertiesByOrderByPropertyPriceAscPropertyPriorityAsc();

    List<Properties> getPropertiesByOrderBySquareMetersDescPropertyPriorityAsc();

    List<Properties> getPropertiesByOrderBySquareMetersAscPropertyPriorityAsc();

    List<Properties> findByIsAvailable(boolean b);

    @Query("select p from Properties p where " +
            "lower(p.propertyTitle) like lower(concat('%',:searchKey,'%')) or " +
            "lower(p.propertyDescription) like lower(concat('%',:searchKey,'%')) or " +
            "lower(p.propertyType) like lower(concat('%',:searchKey,'%')) or " +
            "lower(p.propertyInterior) like lower(concat('%',:searchKey,'%'))" +
            "order by p.propertyPriority asc "
    )
    List<Properties> findPropertiesByKey(@Param("searchKey") String keyword);

    @Query("select p from Properties p where " +
            "(:optionType is null or p.propertyTypeTransaction = :optionType )" +
            "and (:city is null or p.address.province = :city )" +
            "and (:district is null or p.address.district = :district )" +
            "and (:ward is null or p.address.ward = :ward )" +
            "and (:houseType is null or p.propertyType = :houseType )" +
            "and (:priceMin is null or p.propertyPrice >= :priceMin )" +
            "and (:priceMax is null or p.propertyPrice <= :priceMax )" +
            "and (:sqmtMin is null or p.squareMeters>= :sqmtMin )" +
            "and (:sqmtMax is null or p.squareMeters <= :sqmtMax )" +
            "order by p.propertyPriority asc ")
    List<Properties> findByForm(@Param("optionType") String optionType,
                                @Param("city") String city,
                                @Param("district") String district,
                                @Param("ward") String ward,
                                @Param("houseType") String houseType,
                                @Param("priceMin") Double priceMin,
                                @Param("priceMax") Double priceMax,
                                @Param("sqmtMin") Double sqmtMin,
                                @Param("sqmtMax") Double sqmtMax);


    List<Properties> findByAddress_Province(String province);

    @Query("select p from Properties p where " +
            "(:city is null or p.address.province = :city )" +
            "and (:houseType is null or p.propertyType = :houseType )" +
            "and (:priceMin is null or p.propertyPrice >= :priceMin )" +
            "and (:priceMax is null or p.propertyPrice <= :priceMax )" +
            "and (:sqmtMin is null or p.squareMeters>= :sqmtMin )" +
            "and (:sqmtMax is null or p.squareMeters <= :sqmtMax )" +
            "and (:bedroom is null or p.bedrooms = :bedroom)" +
            "order by p.propertyPriority asc ")
    List<Properties> findByCity(@Param("city") String city,
                                @Param("houseType") String houseType,
                                @Param("priceMin") Double priceMin,
                                @Param("priceMax") Double priceMax,
                                @Param("sqmtMin") Double sqmtMin,
                                @Param("sqmtMax") Double sqmtMax,
                                @Param("bedroom") Integer bedroom);

    @Query("select p from Properties p where " +
            "(p.address.province = :province or :province is null) and " +
            "(p.address.district = :district or :district is null) and " +
            "(p.address.ward = :ward or :ward is null)")
    List<Properties> findByAddress(@Param("province") String province,
                                   @Param("district") String district,
                                   @Param("ward") String ward);


    @Query("select p from Properties p where " +
            "(p.address.province = :city or :city is null)" +
            "order by p.propertyPrice asc ,p.propertyPriority asc ")
    List<Properties> sortPriceByCityASC(@Param("city") String city);

    @Query("select p from Properties p where " +
            "(p.address.province = :city or :city is null)" +
            "order by p.propertyPrice desc,p.propertyPriority asc  ")
    List<Properties> sortPriceByCityDESC(@Param("city") String city);

    @Query("select p from Properties p where " +
            "(p.address.province = :city or :city is null)" +
            "order by p.squareMeters asc ,p.propertyPriority asc   ")
    List<Properties> sortSqftByCityASC(@Param("city") String city);

    @Query("select p from Properties p where " +
            "(p.address.province = :city or :city is null)" +
            "order by p.squareMeters desc ,p.propertyPriority asc ")
    List<Properties> sortSqftByCityDESC(@Param("city") String city);

    List<Properties> findByPropertyStatus(String propertyStatus);

    List<Properties> findAllByOrderByPropertyPriorityAsc();
}
