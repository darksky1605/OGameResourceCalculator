package darksky.ogameresourcecalculator;

/**
 * Wrapper class to represent a trade of resources. The values of from, middle and
 * to indicate the resource type. 0 = metal, 1 = crystal, 2 = deuterium
 * 
 * @author ogame.de Dark Sky
 *
 */
public class Trade {
	public int from = 0;
	public int to = 0;
	public int middle = -1;
	public long fromAmount = 0;
	public long toAmount = 0;
	public long middleAmount = 0;

	@Override
	public String toString() {
		return "Trade [from=" + from + ", to=" + to + ", middle=" + middle + ", fromAmount=" + fromAmount
				+ ", toAmount=" + toAmount + ", middleAmount=" + middleAmount + "]";
	}
}
