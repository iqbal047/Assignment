package com.assingment.spectrumapplication.dao;

import com.assingment.spectrumapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndActive(@NonNull String username, boolean isActive);

    @Query(value = "" +
            "select u.username " +
            "from api a " +
            "         join user u on a.user_id = u.id " +
            "where MD5(concat(:randomStr, a.api_private, :publicKey)) = :hash", nativeQuery = true)
    String retrieveUsernameByPublicKeyAndPrivateKey(@Param("randomStr") String randomStr,
                                                    @Param("publicKey") String publicKey,
                                                    @Param("hash") String hash);
}