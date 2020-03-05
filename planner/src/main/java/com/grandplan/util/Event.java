package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostUsername;
    private String eventName;
    private String date;
    private String startTime;
    private String endTime;
    private boolean allDay;

//    @OneToMany(mappedBy = "event")
    @OneToMany(targetEntity = Invite.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Invite> invites;
}
