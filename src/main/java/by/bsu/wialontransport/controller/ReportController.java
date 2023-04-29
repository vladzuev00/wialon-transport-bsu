package by.bsu.wialontransport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public final class ReportController {
    private static final String NAME_OF_PAGE_WITH_REPORTS = "report-page";

    @GetMapping
    public String returnNameOfPageWithReports() {
        return NAME_OF_PAGE_WITH_REPORTS;
    }
}
