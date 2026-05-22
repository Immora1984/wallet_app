package ru.demo.merch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.demo.merch.impl.jpa.Merch;

import java.util.UUID;

public interface MerchRepository extends JpaRepository<Merch, UUID>, JpaSpecificationExecutor<Merch> {
}
