package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.UserActionPairDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserActionRepository extends CrudRepository<UserAction, Long> {

    @Query("select ua.ip_address as ip_address, count(ua.ip_address) as count\n" +
            "from user_action as ua\n" +
            "group by ua.ip_address\n" +
            "order by ua.ip_address desc\n" +
            "limit :limit offset :offset ")
    List<UserActionPairDto> getCountPairsByIpAddresses(Integer limit, Long offset);

    @Query("select count(*) \n" +
            "from(\n" +
            "    select count(ua.ip_address)\n" +
            "    from user_action as ua\n" +
            "    group by ua.ip_address\n" +
            ") as count ")
    Long countAllIpAddresses();

    List<UserAction> findUserActionsByIpAddressOrderByTimeDesc(String ipAddress, Pageable pageable);

    Long countByIpAddress(String ipAddress);

}
