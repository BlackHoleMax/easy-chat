package devblackholemax.easychattingroom.service;

import devblackholemax.easychattingroom.domain.InviteCode;

public interface InviteCodeService {
    public InviteCode getInviteCodeByCode(String code);

    public void addInviteCode(String code);
}
