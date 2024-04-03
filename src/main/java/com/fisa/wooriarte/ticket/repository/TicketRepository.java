package com.fisa.wooriarte.ticket.repository;

import com.fisa.wooriarte.ticket.domain.Ticket;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findByUserIdAndStatusAndCanceled(String user_id, boolean status, boolean canceled);
}
