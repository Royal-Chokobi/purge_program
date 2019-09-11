package kollus.jwt;

import java.util.ArrayList;
import java.util.List;

public class Payload {

	private String cuid;
	private long expt;
	private List<MediaItem> mc;

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public long getExpt() {
		return expt;
	}

	public void setExpt(long expt) {
		this.expt = expt;
	}

	public List<MediaItem> getMc() {
		return mc;
	}

	public void setMc(List<MediaItem> mc) {
		this.mc = mc;
	}

	public void addMediaItem(MediaItem item) {
		if (mc == null) {
			mc = new ArrayList<MediaItem>();
		}
	}

	public void removeMediaItem(String mck) {

	}

	public Payload(String _cuid, long _expt, List<MediaItem> _mc) {
		this.cuid = _cuid;
		this.expt = _expt;
		this.mc = _mc;
	}

	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\r\n");
		if (cuid != null) {
			sb.append(String.format("\"cuid\":\"%s\"\r\n", cuid));
		}
		if (expt > 0) {
			sb.append(String.format("\"expt\":%d\r\n", cuid));
		}
		if (mc != null && mc.size() > 0) {
			sb.append("\"mc\": [ \r\n");
			for (int idx = 0; idx < mc.size(); idx++) {
				MediaItem item = mc.get(idx);
				sb.append("{");
				sb.append("\r\n");
				if (item.getMckey() != null) {
					sb.append(String.format("\"mckey\":\"%s\"\r\n", item.getMckey()));
				}
				if (item.getMckey() != null) {
					sb.append(String.format("\"mcpf\":\"%s\"\r\n", item.getMcpf()));
				}
				if (item.isIntr()) {
					sb.append("\"intr\":\"true\"\r\n");
				}
				if (!item.isSeek()) {
					sb.append("\"seek\":\"false\"\r\n");
				}
				if (item.getSeekable_end() > 0) {
					sb.append(String.format("\"seekable_end\":%d\r\n", item.getSeekable_end()));
				}
				if (item.isDisable_playrate()) {
					sb.append("\"disable_playrate\":\"true\"\r\n");
				}
				sb.append("}");
				if (idx < mc.size() - 1) {
					sb.append(",\r\n");
				} else {
					sb.append("\r\n");
				}
			}
			sb.append("] \r\n");
		}
		sb.append("\r\n");
		sb.append("}");
		return sb.toString();
	}
}
