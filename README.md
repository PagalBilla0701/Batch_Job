package com.scb.cems.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.scb.cems.formbeans.*;
import com.scb.cems.util.Constants;

public class FormServiceImplTest {

    private FormServiceImpl formService;

    @Before
    public void setUp() {
        formService = new FormServiceImpl();
    }

    @Test
    public void testLoadPartialRepaymentInitialData() {
        FormBase formBase = formService.loadPartialRepaymentInitialData();

        assertNotNull(formBase);
        assertEquals("DISPLAY", formBase.getStatus());
        assertEquals("PARTIAL_REPAYMENT_COMPUTATION", formBase.getType());
        assertNotNull(formBase.getData());

        PartialRepaymentForm prForm = (PartialRepaymentForm) formBase.getData();
        assertEquals("Mortgage", prForm.getAccountType());
        assertEquals(Long.valueOf(10L), prForm.getEmployeeRate());
        assertEquals(Long.valueOf(10000L), prForm.getPrincipal());
    }

    @Test
    public void testLoadC400NoteInitialData() {
        C400NoteForm c400NoteForm = formService.loadC400NoteInitialData("ACTIVE");

        assertNotNull(c400NoteForm);
        assertEquals("1010101", c400NoteForm.getOwner());
        assertNotNull(c400NoteForm.getCreatedDate());
        assertEquals("", c400NoteForm.getDescription());
    }

    @Test
    public void testLoadRisNoteInitialData() {
        RisNoteForm risNoteForm = formService.loadR1sNoteInitialData("ACTIVE");

        assertNotNull(risNoteForm);
        assertEquals("1010101", risNoteForm.getCreatedBy());
        assertNotNull(risNoteForm.getCreatedDate());
        assertEquals("", risNoteForm.getDescription());
    }

    @Test
    public void testLoadCCMSNoteInitialData() {
        CCMSNoteForm ccmsNoteForm = formService.loadCCMSNoteInitialData("ACTIVE");

        assertNotNull(ccmsNoteForm);
        assertEquals("ADMIN", ccmsNoteForm.getCreatedBy());
        assertNotNull(ccmsNoteForm.getCreatedDate());
        assertNotNull(ccmsNoteForm.getActionCompleteIndicator());
        assertEquals("CCMS Card Note Details", ccmsNoteForm.getTitle());
    }

    @Test
    public void testLoadCCMSCustomerInitialData() {
        CCMSCustomerForm ccmsCustomerForm = formService.loadCCMSCustomerInitialData("ACTIVE");

        assertNotNull(ccmsCustomerForm);
        assertEquals("ADMIN", ccmsCustomerForm.getCreatedBy());
        assertNotNull(ccmsCustomerForm.getCreatedDate());
        assertNotNull(ccmsCustomerForm.getActionCompleteIndicator());
        assertEquals("CCMS Customer Note Details", ccmsCustomerForm.getTitle());
    }

    @Test
    public void testLoadPartialRepaymentInitialData_NullCheck() {
        FormBase formBase = formService.loadPartialRepaymentInitialData();
        assertNotNull("FormBase should not be null", formBase);
    }

    @Test
    public void testLoadC400NoteInitialData_Negative() {
        C400NoteForm c400NoteForm = formService.loadC400NoteInitialData("INACTIVE");

        assertNotNull(c400NoteForm);
        assertNotEquals("Invalid Owner", c400NoteForm.getOwner());
    }

    @Test
    public void testLoadRisNoteInitialData_Negative() {
        RisNoteForm risNoteForm = formService.loadR1sNoteInitialData("INACTIVE");

        assertNotNull(risNoteForm);
        assertNotEquals("Invalid Created By", risNoteForm.getCreatedBy());
    }

    @Test
    public void testLoadCCMSNoteInitialData_Negative() {
        CCMSNoteForm ccmsNoteForm = formService.loadCCMSNoteInitialData("INACTIVE");

        assertNotNull(ccmsNoteForm);
        assertNotEquals("Invalid Title", ccmsNoteForm.getTitle());
    }

    @Test
    public void testLoadCCMSCustomerInitialData_Negative() {
        CCMSCustomerForm ccmsCustomerForm = formService.loadCCMSCustomerInitialData("INACTIVE");

        assertNotNull(ccmsCustomerForm);
        assertNotEquals("Invalid Title", ccmsCustomerForm.getTitle());
    }
}
