package devblackholemax.easychattingroom.dao;

import devblackholemax.easychattingroom.domain.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {
    InviteCode findInviteCodeByCode(String code);
}
