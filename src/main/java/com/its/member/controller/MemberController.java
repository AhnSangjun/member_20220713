package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm() {
        return "memberPages/save";
    }

    @GetMapping("/login-form")
    public String loginForm(@RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL,
                            Model model) {
        model.addAttribute("redirectURL", redirectURL);
        return "memberPages/login";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("id", loginResult.getId());
            return "memberPages/my-page";
        } else {
            return "memberPages/login";
        }
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList",memberDTOList);
        return "memberPages/list";
    }

//   /member/3
//   /member?id=3
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/detail";
    }

    // ajax ????????????
    @PostMapping("/ajax/{id}")
    public @ResponseBody MemberDTO findByIdAjax(@PathVariable Long id) {
        MemberDTO memberDTO = memberService.findById(id);
        return memberDTO;
    }

    // get ?????? ?????? /member/delete/3
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/member/";
//        return "memberPages/list"; // x
    }

    /**
     * /member/3 : ??????(get) R, ??????(save) C, ??????(put) U, ??????(delete) D
     */

    // delete ?????? ??????
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAjax(@PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK); // ajax ????????? ?????? ???????????? 200 ????????? ???.
    }

    // ???????????? ??????
    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("updateMember", memberDTO);
        return "memberPages/update";
    }

    // ????????????
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/"+memberDTO.getId();
    }

    // ????????????(put ??????)
    @PostMapping("/{id}")
    public ResponseEntity updateByAjax(@RequestBody MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ????????? ????????????
    @PostMapping("/emailCheck")
    public @ResponseBody String emailCheck(@RequestParam String memberEmail) {
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // ?????? ????????? ?????????
        session.removeAttribute("loginEmail"); // login Email ??? ?????? ??????
        return "redirect:/";
    }

}
