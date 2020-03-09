package com.grandplan.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.server.repositories.InviteRepo;
import com.grandplan.util.Invite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.boot.CommandLineRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Component
@Slf4j
public class ApiInviteService {

    private final InviteRepo inviteRepo;

    public List<Invite> getInvites() {
        return inviteRepo.findAll();
    }

    public Set<Invite> getUserInvites(String email) {
        return inviteRepo.findInvitesByEmail(email);
    }

    public Invite createInvite(Invite invite) {
        inviteRepo.save(invite);
        return invite;
    }

    public Boolean deleteInvite(Invite invite) {
        if (inviteRepo.findInviteById(invite.getId()) == null) {
            return false;
        }
        inviteRepo.delete(invite);
        return true;
    }

    public Invite updateInvite(Invite invite, Boolean accepted) {

        if (invite == null)
        {
            log.info("Unable to update invite");
            return null;
        }
        inviteRepo.update(
                accepted,
                invite.getId()
        );
        return inviteRepo.findInviteById(invite.getId());
    }
    private void writeListOfInvites(List<Invite> invites) {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsonObjects = new JSONArray();
        for (Invite inv : invites) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("inviteId", inv.getId());
            jsonObject.put("userId", inv.getUser().getId());
            jsonObject.put("eventId", inv.getEvent().getId());
            jsonObject.put("accepted", inv.getAccepted());
            jsonObjects.add(jsonObject);
        }
        try (FileWriter file = new FileWriter("src/main/resources/data/Invites.json", false)) {
            //false indicates that Invites.json will get overridden with the current user data
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObjects);
            file.write(indented);
            log.info("JSON file updated: " + jsonObjects);
        } catch (IOException e) {
            log.info("Unable to write Invites to Invites.json");
            log.error(e.getMessage());
        }
    }

}
