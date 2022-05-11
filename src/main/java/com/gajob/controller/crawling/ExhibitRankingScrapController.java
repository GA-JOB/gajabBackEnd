package com.gajob.controller.crawling;

import com.gajob.service.crawling.ExhibitRankingScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/issue/exhibit-ranking")
@CrossOrigin(origins = "http://localhost:3000/")
public class ExhibitRankingScrapController {

    private final ExhibitRankingScrapService exhibitRankingScrapService;

    @PostMapping("/scrap/{exhibitRankingId}")   // 랭킹 공모전 스크랩 기능
    public String scrap(@PathVariable Long exhibitRankingId) {
        return exhibitRankingScrapService.scrap(exhibitRankingId);
    }

    @GetMapping("/scrap")   // 스크랩한 랭킹 공모전 전체 조회
    public ResponseEntity getScrap() {
        return ResponseEntity.ok(exhibitRankingScrapService.getScrap());
    }

}
