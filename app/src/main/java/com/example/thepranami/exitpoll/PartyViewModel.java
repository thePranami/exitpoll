package com.example.thepranami.exitpoll;

public class PartyViewModel {
    String Id, PartyName, CandidateName, ImageUrl;

    public PartyViewModel(String id, String partyName, String candidateName, String imageUrl) {
        Id = id;
        PartyName = partyName;
        CandidateName = candidateName;
        ImageUrl = imageUrl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getCandidateName() {
        return CandidateName;
    }

    public void setCandidateName(String candidateName) {
        CandidateName = candidateName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
