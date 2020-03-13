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

    public Set<Invite> getEventInvites(Long id) {
        return inviteRepo.findInvitesByEvent(id);
    }

    public Invite getUserEventInvite(String email, Long id){
        return inviteRepo.findInviteByEventAndUser(email, id);
    }

    public Invite createInvite(Invite invite) {
        if (inviteRepo.findInviteById(invite.getId()) != null) {
            return inviteRepo.findInviteById(invite.getId());
        }
        inviteRepo.save(invite);
        writeListOfInvites(getInvites());
        return invite;
    }

    public Boolean deleteInvite(Long inviteId) {
        Invite invite = inviteRepo.findInviteById(inviteId);
        if (invite == null) {
            return false;
        }
        inviteRepo.delete(invite);
        writeListOfInvites(getInvites());
        return true;
    }

    public Invite updateInvite(Long inviteId, Boolean accepted) {
        if (inviteId == null){
            log.info("Unable to update invite");
            return null;
        }
        inviteRepo.update(
            accepted,
            inviteId
        );
        return inviteRepo.findInviteById(inviteId);
    }

    public Set<Invite> getUnacceptedUserInvites(String email){
        return inviteRepo.findUnacceptedInvitesByEmail(email);
    }

    public Set<Invite> getAcceptedUserInvites(String email){
        return inviteRepo.findAcceptedInvitesByEmail(email);
    }

    public Set<Invite> getUnacceptedEventInvites(Long id){
        return inviteRepo.findUnacceptedInvitesByEvent(id);
    }

    public Set<Invite> getAcceptedEventInvites(Long id){
        return inviteRepo.findAcceptedInvitesByEvent(id);
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
        try (FileWriter file = new FileWriter("src/main/resources/data/Invites2.json", false)) {
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
