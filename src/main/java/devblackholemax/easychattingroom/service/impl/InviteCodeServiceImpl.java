package devblackholemax.easychattingroom.service.impl;

import devblackholemax.easychattingroom.dao.InviteCodeRepository;
import devblackholemax.easychattingroom.domain.InviteCode;
import devblackholemax.easychattingroom.service.InviteCodeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class InviteCodeServiceImpl implements InviteCodeService {
    @Resource
    InviteCodeRepository inviteCodeRepository;

    @Override
    public InviteCode getInviteCodeByCode(String code) {
        return inviteCodeRepository.findInviteCodeByCode(code);
    }

    @Override
    public void addInviteCode(String code) {
        InviteCode inviteCode = new InviteCode();
        inviteCode.setCode(code);
        inviteCodeRepository.save(inviteCode);
    }
}
