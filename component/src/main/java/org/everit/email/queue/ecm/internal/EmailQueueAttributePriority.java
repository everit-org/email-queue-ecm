/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.email.queue.ecm.internal;

/**
 * Constants of priority.
 */
public final class EmailQueueAttributePriority {

  public static final int P_EMAIL_SENDER = 4;

  public static final int P_EMAIL_STORE = 3;

  public static final int P_BATCH_MAX_SIZE = 5;

  public static final int P_QUERYDSL_SUPPORT = 1;

  public static final int P_SERVICE_DESCRIPTION = 0;

  public static final int P_TRANSACTION_PROPAGATOR = 2;

  private EmailQueueAttributePriority() {
  }
}
