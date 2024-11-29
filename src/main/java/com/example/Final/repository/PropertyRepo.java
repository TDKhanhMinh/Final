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

    @Query("select p from Properties p where " +
            "lower(p.propertyTitle) like lower(concat('%',:searchKey,'%')) or " +
            "lower(p.propertyDescription) like lower(concat('%',:searchKey,'%')) or " +
            "lower(p.propertyType) like lower(concat('%',:searchKey,'%')) or " +
            "lower(p.propertyInterior) like lower(concat('%',:searchKey,'%'))"
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
            "and (:sqmtMax is null or p.squareMeters <= :sqmtMax )")
    List<Properties> findByForm(@Param("optionType") String optionType,
                                @Param("city") String city,
                                @Param("district") String district,
                                @Param("ward") String ward,
                                @Param("houseType") String houseType,
                                @Param("priceMin") Double priceMin,
                                @Param("priceMax") Double priceMax,
                                @Param("sqmtMin") Double sqmtMin,
                                @Param("sqmtMax") Double sqmtMax);
}
