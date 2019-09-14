package com.psawesome.demorestapi.events;

import com.psawesome.demorestapi.common.BaseControllerTest;
import com.psawesome.demorestapi.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Before
    public void setEventRepository() {
        Event event = Event.builder()
                .name("Spring No 1")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8, 28, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8, 29, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 30, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 31, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("True the door")
                .build();

        eventRepository.save(event);
    }

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8, 28, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8, 29, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 30, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 31, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("True the door")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event"
                        , links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update and existing event"),
                                linkWithRel("profile").description("link to profile")
                        )
                        , requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        )
                        , requestFields(
                                fieldWithPath("name").description("Name of new event")
                                , fieldWithPath("description").description("description of new event")
                                , fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event")
                                , fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event")
                                , fieldWithPath("beginEventDateTime").description("date time of begin of new event")
                                , fieldWithPath("endEventDateTime").description("date time of end of new event")
                                , fieldWithPath("location").description("location of new event")
                                , fieldWithPath("basePrice").description("base price of new event")
                                , fieldWithPath("maxPrice").description("max price of new event")
                                , fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        )
                        , responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        )
                        , responseFields(
                                fieldWithPath("id").description("identifier of new event")
                                , fieldWithPath("name").description("Name of new event")
                                , fieldWithPath("description").description("description of new event")
                                , fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event")
                                , fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event")
                                , fieldWithPath("beginEventDateTime").description("date time of begin of new event")
                                , fieldWithPath("endEventDateTime").description("date time of end of new event")
                                , fieldWithPath("location").description("location of new event")
                                , fieldWithPath("basePrice").description("base price of new event")
                                , fieldWithPath("maxPrice").description("max price of new event")
                                , fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                                , fieldWithPath("free").description("it tells if this event is free or not")
                                , fieldWithPath("offline").description("it tells if this event is online or offline")
                                , fieldWithPath("eventStatus").description("event status")
                                , fieldWithPath("_links.self.href").description("link to self")
                                , fieldWithPath("_links.query-events.href").description("link to query event list")
                                , fieldWithPath("_links.update-event.href").description("link to update existing event")
                                , fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8, 28, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8, 29, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 30, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 31, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    /**
     *
     */
    @Test
    @TestDescription("필수 입력 값이 비어있는 경우 에러가 발생하는 테스트")
    public void createEvent_bad_request_empty_input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        //@formatter:off
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
            .andExpect(status().isBadRequest());
        //@formatter:on
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_bad_request_wrong_input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8, 28, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8, 29, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 30, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 28, 14, 21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();
        //@formatter:off
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
        //@formatter:on
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두 번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(this::getGeneratedEvent);

        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events",
                        links(
                                linkWithRel("first").description("link to first page"),
                                linkWithRel("next").description("link to next page"),
                                linkWithRel("last").description("link to last page"),
                                linkWithRel("prev").description("link to previous page"),
                                linkWithRel("self").description("link to self page"),
                                linkWithRel("profile").description("link to page list")
                        )
                        )
                )
        ;

    }

    private Event getGeneratedEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8, 28, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8, 29, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 30, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 31, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }

    @Test
    @TestDescription("기존의 이벤트 하나 조회하기")
    public void getEvent() throws Exception {
        //Geven
        Event event = this.getGeneratedEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        this.mockMvc.perform(get("/api/events/117"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("Event 정상적으로 수정하기")
    public void testModifyEvent() throws Exception {
        //Given
        Event event = this.getGeneratedEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @TestDescription("입력값이 비어있는 경우 이벤트 수정 실패")
    public void testModify_empty() throws Exception {
        //Given

        Event event = this.getGeneratedEvent(200);

        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우 수정 실패")
    public void testModify400_Wrong() throws Exception {
        //Given
        Event event = this.getGeneratedEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void testModify404() throws Exception {
        //Given
        Event event = this.getGeneratedEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/231", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
