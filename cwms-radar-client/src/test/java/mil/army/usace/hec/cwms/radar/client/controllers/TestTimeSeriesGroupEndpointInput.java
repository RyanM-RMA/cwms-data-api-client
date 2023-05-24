/*
 * MIT License
 *
 * Copyright (c) 2023 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mil.army.usace.hec.cwms.radar.client.controllers;

import mil.army.usace.hec.cwms.radar.client.model.RadarObjectMapper;
import mil.army.usace.hec.cwms.radar.client.model.TimeSeriesGroup;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static mil.army.usace.hec.cwms.radar.client.controllers.RadarEndpointConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cwms.radar.client.controllers.RadarEndpointConstants.ACCEPT_QUERY_HEADER;
import static mil.army.usace.hec.cwms.radar.client.controllers.TestController.readJsonFile;
import static mil.army.usace.hec.cwms.radar.client.controllers.TimeSeriesGroupEndpointInput.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTimeSeriesGroupEndpointInput {

    @Test
    void testGetOne() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        TimeSeriesGroupEndpointInput.GetOne input = TimeSeriesGroupEndpointInput.getOne("category-id", "group-id", "SWT");
        input.addInputParameters(mockHttpRequestBuilder);
        assertEquals("category-id", mockHttpRequestBuilder.getQueryParameter(CATEGORY_ID_QUERY_PARAMETER));
        assertEquals("SWT", mockHttpRequestBuilder.getQueryParameter(OFFICE_QUERY_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
    }

    @Test
    void testGetAll() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        TimeSeriesGroupEndpointInput.GetAll input = TimeSeriesGroupEndpointInput.getAll().officeId("SWT");
        input.addInputParameters(mockHttpRequestBuilder);
        assertEquals("SWT", mockHttpRequestBuilder.getQueryParameter(OFFICE_QUERY_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
    }

    @Test
    void testPost() throws IOException {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        String collect = readJsonFile("radar/v1/json/ts_group.json");
        TimeSeriesGroup timeSeriesGroup = RadarObjectMapper.mapJsonToObject(collect, TimeSeriesGroup.class);
        TimeSeriesGroupEndpointInput.Post input = TimeSeriesGroupEndpointInput.post(timeSeriesGroup)
                .failIfExists(true);
        input.addInputParameters(mockHttpRequestBuilder);
        assertEquals("true", mockHttpRequestBuilder.getQueryParameter(FAIL_IF_EXISTS));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
    }

    @Test
    void testPatch() throws IOException {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        String collect = readJsonFile("radar/v1/json/ts_group.json");
        TimeSeriesGroup timeSeriesGroup = RadarObjectMapper.mapJsonToObject(collect, TimeSeriesGroup.class);
        TimeSeriesGroupEndpointInput.Patch input = TimeSeriesGroupEndpointInput.patch("group-id", timeSeriesGroup)
                .replaceAssignedTs(true);
        input.addInputParameters(mockHttpRequestBuilder);
        assertEquals("true", mockHttpRequestBuilder.getQueryParameter(REPLACE_ASSIGNED_TS_QUERY_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
    }

    @Test
    void testDelete() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        TimeSeriesGroupEndpointInput.Delete input = TimeSeriesGroupEndpointInput.delete("category-id", "group-id", "SWT");
        input.addInputParameters(mockHttpRequestBuilder);
        assertEquals("category-id", mockHttpRequestBuilder.getQueryParameter(CATEGORY_ID_QUERY_PARAMETER));
        assertEquals("SWT", mockHttpRequestBuilder.getQueryParameter(OFFICE_QUERY_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
    }
}
