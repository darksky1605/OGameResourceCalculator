package darksky.ogameresourcecalculator;

/**
 * Wrapper class to store resource production and exchange rates
 * 
 * @author ogame.de Dark Sky
 *
 */
public class Profile {
	public String name = "default";
	public long prodMet = 0;
	public long prodKris = 0;
	public long prodDeut = 0;
	public double rateMM = 0.0;
	public double rateMK = 0.0;
	public double rateMD = 0.0;
	public double rateKM = 0.0;
	public double rateKK = 0.0;
	public double rateKD = 0.0;
	public double rateDM = 0.0;
	public double rateDK = 0.0;
	public double rateDD = 0.0;

	public Profile() {
	}

	public Profile(String name) {
		super();
		this.name = name;
	}

	public Profile(String name, long prodMet, long prodKris, long prodDeut, double rateMM, double rateMK, double rateMD,
			double rateKM, double rateKK, double rateKD, double rateDM, double rateDK, double rateDD) {
		super();
		this.name = name;
		this.prodMet = prodMet;
		this.prodKris = prodKris;
		this.prodDeut = prodDeut;
		this.rateMM = rateMM;
		this.rateMK = rateMK;
		this.rateMD = rateMD;
		this.rateKM = rateKM;
		this.rateKK = rateKK;
		this.rateKD = rateKD;
		this.rateDM = rateDM;
		this.rateDK = rateDK;
		this.rateDD = rateDD;
	}

	@Override
	public String toString() {
		return "Profile [name=" + name + ", prodMet=" + prodMet + ", prodKris=" + prodKris + ", prodDeut=" + prodDeut
				+ ", rateMM=" + rateMM + ", rateMK=" + rateMK + ", rateMD=" + rateMD + ", rateKM=" + rateKM
				+ ", rateKK=" + rateKK + ", rateKD=" + rateKD + ", rateDM=" + rateDM + ", rateDK=" + rateDK
				+ ", rateDD=" + rateDD + "]";
	}

}