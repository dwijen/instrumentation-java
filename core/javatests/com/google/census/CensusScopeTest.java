/*
 * Copyright 2016, Google Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 *    * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.google.census;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CensusScope}
 */
@RunWith(JUnit4.class)
public class CensusScopeTest {
  @Test
  public void testNoScope() {
    assertEquals(CensusContextFactory.getCurrent(), CensusContextFactory.getDefault());
  }

  @Test
  public void testExplicitScope() {
    CensusScope scope = new CensusScope(TAG_MAP1);
    assertEquals(
        CensusContextFactory.getDefault().with(TAG_MAP1),
        CensusContextFactory.getCurrent());
    scope.close();
    assertEquals(CensusContextFactory.getDefault(), CensusContextFactory.getCurrent());
  }

  @Test
  public void testImplicitScope() {
    try (CensusScope scope = new CensusScope(TAG_MAP1)) {
      assertEquals(
          CensusContextFactory.getDefault().with(TAG_MAP1),
          CensusContextFactory.getCurrent());
    }
    assertEquals(CensusContextFactory.getDefault(), CensusContextFactory.getCurrent());
  }

  @Test
  public void testNestedScope() {
    try (CensusScope s1 = new CensusScope(TAG_MAP1)) {
      assertEquals(
          CensusContextFactory.getDefault().with(TAG_MAP1),
          CensusContextFactory.getCurrent());
      try (CensusScope s2 = new CensusScope(TAG_MAP2)) {
        assertEquals(
             CensusContextFactory.getDefault().with(TAG_MAP1).with(TAG_MAP2),
            CensusContextFactory.getCurrent());
      }
      assertEquals(
          CensusContextFactory.getDefault().with(TAG_MAP1),
          CensusContextFactory.getCurrent());
    }
    assertEquals(CensusContextFactory.getDefault(), CensusContextFactory.getCurrent());
  }

  private static final TagMap TAG_MAP1 = TagMap.of(new TagKey("k1"), "v1");
  private static final TagMap TAG_MAP2 = TagMap.of(new TagKey("k2"), "v2");
}
