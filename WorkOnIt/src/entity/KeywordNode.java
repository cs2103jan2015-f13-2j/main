package entity;

import java.util.ArrayList;
import java.util.List;

public class KeywordNode {

	String keyword;
	List<KeywordNode> subsequentKeywords;

	public KeywordNode() {
		this(new String(), new ArrayList<KeywordNode>());

	}

	public KeywordNode(String keyword) {
		this(keyword, new ArrayList<KeywordNode>());

	}

	private KeywordNode(String keyword, List<KeywordNode> subsequentKeywords) {
		this.setKeyword(keyword);
		this.setSubsequentKeywords(subsequentKeywords);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<KeywordNode> getSubsequentKeywords() {
		return subsequentKeywords;
	}

	private void setSubsequentKeywords(List<KeywordNode> subsequentKeywords) {
		this.subsequentKeywords = subsequentKeywords;
	}

	public void addSubsequentKeywords(KeywordNode node) {

		if (subsequentKeywords != null) {
			subsequentKeywords.add(node);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			String other = (String) obj;
			if (keyword == null) {
				if (other != null)
					return false;
			} else if (!keyword.equals(other))
				return false;
		}
		return true;
	}
}
