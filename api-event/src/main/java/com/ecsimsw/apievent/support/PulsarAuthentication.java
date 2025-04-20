
package com.ecsimsw.apievent.support;

import org.apache.pulsar.client.api.Authentication;
import org.apache.pulsar.client.api.AuthenticationDataProvider;
import org.apache.pulsar.shade.org.apache.commons.codec.digest.DigestUtils;

import java.io.Serial;
import java.util.Map;

public class PulsarAuthentication implements Authentication {

	private static final long serialVersionUID = 5208196920897870764L;

	private final String accessId;
	private final String accessKey;

	public PulsarAuthentication(String accessId, String accessKey) {
		this.accessId = accessId;
		this.accessKey = accessKey;
	}

	@Override
	public String getAuthMethodName() {
		return "auth1";
	}

	@Override
	public AuthenticationDataProvider getAuthData() {
		return new TuyaPulsarAuthenticationDataProvider(
			this.accessId,
			this.accessKey
		);
	}

	@Override
	public void configure(Map<String, String> map) {
	}

	@Override
	public void start() {
	}

	@Override
	public void close() {
	}

	static class TuyaPulsarAuthenticationDataProvider implements AuthenticationDataProvider {

		private static final long serialVersionUID = -4762711950367592920L;
		private final String commandData;

		public TuyaPulsarAuthenticationDataProvider(String accessId, String accessKey) {
			this.commandData = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", accessId,
				DigestUtils.md5Hex(accessId + DigestUtils.md5Hex(accessKey)).substring(8, 24));
		}

		@Override
		public String getCommandData() {
			return commandData;
		}

		@Override
		public boolean hasDataForHttp() {
			return false;
		}

		@Override
		public boolean hasDataFromCommand() {
			return true;
		}
	}
}
