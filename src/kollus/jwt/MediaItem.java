package kollus.jwt;

public class MediaItem {
	private String mckey;
	private String mcpf;
	private boolean intr = false;
	private boolean seek = true;
	private int seekable_end = -1;
	private boolean disable_playrate = false;

	public String getMckey() {
		return mckey;
	}

	public void setMckey(String mckey) {
		this.mckey = mckey;
	}

	public String getMcpf() {
		return mcpf;
	}

	public void setMcpf(String mcpf) {
		this.mcpf = mcpf;
	}

	public boolean isIntr() {
		return intr;
	}

	public void setIntr(boolean intr) {
		this.intr = intr;
	}

	public boolean isSeek() {
		return seek;
	}

	public void setSeek(boolean seek) {
		this.seek = seek;
	}

	public int getSeekable_end() {
		return seekable_end;
	}

	public void setSeekable_end(int seekable_end) {
		this.seekable_end = seekable_end;
	}

	public boolean isDisable_playrate() {
		return disable_playrate;
	}

	public void setDisable_playrate(boolean disable_playrate) {
		this.disable_playrate = disable_playrate;
	}

	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if (mckey != null) {
			sb.append("\"mckey\":");
			sb.append("\"");
			sb.append(mckey);
			sb.append("\"");
		}
		
		if (mcpf != null) {
			if (!sb.substring(sb.length() - 1, sb.length()).equals("{")
					&& !sb.substring(sb.length() - 1, sb.length()).equals(",")) {
				sb.append(",");
			}
			sb.append("\"mcpf\":");
			sb.append("\"");
			sb.append(mcpf);
			sb.append("\"");
		}
		
		if (intr) {
			if (!sb.substring(sb.length() - 1, sb.length()).equals("{")
					&& !sb.substring(sb.length() - 1, sb.length()).equals(",")) {
				sb.append(",");
			}
			sb.append("\"intr\":");
			sb.append(intr);
		}
		if (!seek) {
			if (!sb.substring(sb.length() - 1, sb.length()).equals("{")
					&& !sb.substring(sb.length() - 1, sb.length()).equals(",")) {
				sb.append(",");
			}
			sb.append("\"seek\":");
			sb.append(intr);
		}
		if (seekable_end != -1) {
			if (!sb.substring(sb.length() - 1, sb.length()).equals("{")
					&& !sb.substring(sb.length() - 1, sb.length()).equals(",")) {
				sb.append(",");
			}
			sb.append("\"seekable_end\":");
			sb.append(seekable_end);
		}
		if (disable_playrate) {
			if (!sb.substring(sb.length() - 1, sb.length()).equals("{")
					&& !sb.substring(sb.length() - 1, sb.length()).equals(",")) {
				sb.append(",");
			}
			sb.append("\"disable_playrate\":");
			sb.append(disable_playrate);
		}
		sb.append("}");
		return sb.toString();
	}
}
