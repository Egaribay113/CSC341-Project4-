/**
 * The Bank
 */



public class BankImpl implements Bank
{
	private int n;			// the number of threads in the system
	private int m;			// the number of resources

	private int[] available; 	// the amount available of eah resource 
	private int[][] maximum; 	// the maximum demand of each thread 
	private int[][] allocation;	// the amount currently allocated to each thread	
	private int[][] need;		// the remaining needs of each thread
	


	/**
	 * Create a new bank with resources.
	 */
	public BankImpl(int[] resources) {
		// m is the number of resources
		m = resources.length;
		n = TestBankers.NUMBER_OF_CUSTOMERS;

		// initialize the resources array
		available = new int[m];
		System.arraycopy(resources,0,available,0,m);

		// create the array for storing the maximum demand by  each thread
		maximum = new int[TestBankers.NUMBER_OF_CUSTOMERS][];
		allocation = new int[TestBankers.NUMBER_OF_CUSTOMERS][];
		need = new int[TestBankers.NUMBER_OF_CUSTOMERS][];
	}

	/**
	 * This method is invoked by a thread when it enters the system. It records
	 * its maximum demand with the bank.
	 */
	public void addCustomer(int threadNum, int[] maxDemand) {
		maximum[threadNum] = new int[m];
		allocation[threadNum] = new int[m];
		need[threadNum] = new int[m];

		System.arraycopy(maxDemand, 0, maximum[threadNum], 0, maxDemand.length);
		System.arraycopy(maxDemand, 0, need[threadNum], 0, maxDemand.length);
	}

	/**
	 * Outputs the state for each thread
	 */

	public void getState() {
		System.out.print("Available = \t[");
		for (int i = 0; i < m-1; i++)
			System.out.print(available[i]+" ");
		System.out.println(available[m-1]+"]");
		
		System.out.print("\nAllocation = ");
		for (int i = 0; i < n; i++) {
			System.out.print("\t[");
			for (int j = 0; j < m-1; j++)
				System.out.print(allocation[i][j]+" ");
			System.out.print(allocation[i][m-1]+"]");
		}
		System.out.print("\nMax = \t");
		for (int i = 0; i < n; i++) {
			System.out.print("\t[");
			for (int j = 0; j < m-1; j++)
				System.out.print(maximum[i][j]+" ");
			System.out.print(maximum[i][m-1]+"]");
		}
		System.out.print("\nNeed = \t");
		for (int i = 0; i < n; i++) {
			System.out.print("\t[");
			for (int j = 0; j < m-1; j++)
				System.out.print(need[i][j]+" ");
			System.out.print(need[i][m-1]+"]");
		}

		System.out.println();
	}


	/**
	 * Determines whether granting a request results in leaving
	 * the system in a safe state or not.
	 *
	 * @return  true - the system is in a safe state.
	 * @return  false - the system is NOT in a safe state.
	 */
	private boolean isSafeState (int threadNum, int[] request) {

		int[] work = new int[m]; //available 
		int[][] tempAlloc = new int[n][m]; //allocation 
		int[][]	tempNeed = new int[n][m]; //Need 
		boolean[] finish = new boolean[n]; //processes that successfully finished



		
		return true;
	}

	/**
	 * Make a request for resources. This is a blocking method that returns
	 * only when the request can safely be satisfied.
	 *
	 * @return  true - the request is granted.
	 * @return  false - the request is not granted.
	 */

	public synchronized boolean requestResources(int threadNum, int[] request)  {
		for (int i = 0; i < request.length; i++) {
			if (request[i] > need[threadNum][i]) {
				System.out.println("Request exceeds need");
				return false;
			}
		}
		if (!isSafeState(threadNum,request)) {
			//System.out.println("Customer # " + threadNum + " is denied.");
			return false;
		}

		// if it is safe, allocate the resources to thread threadNum 
		for (int i = 0; i < m; i++) {
			available[i] -= request[i];
			allocation[threadNum][i] += request[i];
			need[threadNum][i] = maximum[threadNum][i] - allocation[threadNum][i];
		}
		return true;
	}


	/**
	 * Make a request for resources. This is a blocking method that returns
	 * only when the request can safely be satisfied.
	 *
	 * @return  true - the request is granted.
	 * @return  false - the request is not granted.

	public synchronized void requestResources(int threadNum, int[] request) throws InterruptedException {
		while (!isSafeState(threadNum,request))
			wait();

		// if it is safe, allocate the resources to thread threadNum 
		for (int i = 0; i < m; i++) {
                        available[i] -= request[i];
			allocation[threadNum][i] += request[i];
			need[threadNum][i] = maximum[threadNum][i] - allocation[threadNum][i];
		}
                System.out.println("Customer # " + threadNum + " using resources.");
                 System.out.print("Available = ");
                for (int i = 0; i < m; i++)
                    System.out.print(available[i] + "  ");
	}
	 */

	/**
	 * Release resources
	 *
	 * @param int[] release - the resources to be released.
	 */
	public  synchronized void releaseResources(int threadNum, int[] release)  {
		System.out.print("\n Customer # " + threadNum + " releasing ");
		for (int i = 0; i < m; i++) System.out.print(release[i] + " ");

		for (int i = 0; i < m; i++) {
			available[i] += release[i];
			allocation[threadNum][i] -= release[i];
			need[threadNum][i] = maximum[threadNum][i] - allocation[threadNum][i];
		}

		System.out.print("Available = ");
		for (int i = 0; i < m; i++)
			System.out.print(available[i] + "  ");

		System.out.print("Allocated = [");
		for (int i = 0; i < m; i++)
			System.out.print(allocation[threadNum][i] + "  "); 
		System.out.print("]"); 

		// there may be some threads that can now proceed
		//notifyAll();
	}

}
