public class DynamicTimeWarping {

	public static void main(String[] args) {
		int num_samp = 10;
		float [] A = new float [num_samp];
		float [] B = new float [num_samp];
		
		int num_layers = 2*num_samp - 1;
		int win = num_samp;
		int width = 2*win + 1;
		float [][] dist = new float [num_layers][width];
		int [][] toggle = new int [num_layers][width];
		
		for(int i=0;i<num_samp;i++)
		{
			A[i] = i+6;
			B[i] = i+5;
		}
		
		int min_toggle = -1;
		int s1,s2;
		for ( int l=0; l < num_layers; l++ )
		{
			if ( l % 2 == 0 )
			{
				for ( int w=0; w < width; w++ )
				{
					s1 = -win + (l/2) + w;
					if ( s1 >= 0 && s1 >= (l/2) - win && s1 < num_samp)
					{
						s2 = l - s1;
						if ( s2 >= 0 && s2 >= (l/2) - win && s2 < num_samp)
						{
							min_toggle = -1;
							dist[l][w] = 10000;
							if ( l > 0 )
							{
								if (dist[l-1][w] <= dist[l][w])
								{
									dist[l][w] = dist[l-1][w];
									min_toggle = 0;
								}
								if ( w > 0 )
								{
									if (dist[l-1][w-1] <= dist[l][w])
									{
										dist[l][w] = dist[l-1][w-1];
										min_toggle = 1;
									}
								}
							}
							if ( l > 1 )
							{
								if (dist[l-2][w] <= dist[l][w])
								{
									dist[l][w] = dist[l-2][w];
									min_toggle = 2;
								}
							}
							if(min_toggle==-1)
							{
								dist[l][w] = 0;
							}
							toggle[l][w] = min_toggle;
							dist[l][w] += metric(A[s1],B[s2]);
						}
					}
				}
			}
			else
			{
				for ( int w=0; w < width; w++ )
				{
					s1 = -win + (l/2)+1 + w;
					if ( s1 >= 0 && s1 >= (l/2)+1 - win && s1 < num_samp)
					{
						s2 = l - s1;
						if ( s2 >= 0 && s2 >= (l/2)+1 - win && s2 < num_samp)
						{
							min_toggle = -1;
							dist[l][w] = 10000;
							if ( l > 0 )
							{
								if (dist[l-1][w] <= dist[l][w])
								{
									dist[l][w] = dist[l-1][w];
									min_toggle = 0;
								}
								if ( w+1 < width )
								{
									if (dist[l-1][w+1] <= dist[l][w])
									{
										dist[l][w] = dist[l-1][w+1];
										min_toggle = 3;
									}
								}
							}
							if ( l > 1 )
							{
								if (dist[l-2][w] <= dist[l][w])
								{
									dist[l][w] = dist[l-2][w];
									min_toggle = 2;
								}
							}
							if (min_toggle==-1)
							{
								dist[l][w] = 0;
							}
							toggle[l][w] = min_toggle;
							dist[l][w] += metric(A[s1],B[s2]);
						}
					}
				}
			}
		}
		
		int [] shift = new int[num_samp];
		float min_dist = 10000;
		int S1=1;
		int S2=1;
		int L = num_layers-1;
		int W = win;
		while(S1>0&&S2>0)
		{
			S1 = -win + (L/2) + (L%2) + W;
			S2 = L - S1;
			shift[S1] = S2-S1;
			System.out.println(S1+"\t"+S2+"\t"+L+"\t"+W+"\t"+toggle[L][W]);
			if(toggle[L][W] == 0)
			{
				L--;
			}
			else
			if(toggle[L][W] == 1)
			{
				L--;
				W--;
			}
			else
			if(toggle[L][W] == 2)
			{
				L-=2;
			}
			else
			if(toggle[L][W] == 3)
			{
				L--;
				W++;
			}
		}
		
		System.out.println("\t");
		for(int w=0;w<width;w++)
		{
			System.out.print(w+"\t");
		}
		System.out.println();
		System.out.println("dist:");
		for(int l=0;l<num_layers;l++)
		{
			System.out.print(l+"\t");
			for(int w=0;w<width;w++)
			{
				System.out.print(dist[l][w]+"\t");
			}
			System.out.println();
		}
		
		System.out.println("toggle:");
		for(int l=0;l<num_layers;l++)
		{
			System.out.print(l+"\t");
			for(int w=0;w<width;w++)
			{
				System.out.print(toggle[l][w]+"\t");
			}
			System.out.println();
		}
		
		System.out.println("shifts:");
		for(int i=0;i<num_samp;i++)
		{
			System.out.print(shift[i]+"\t");
		}
		System.out.println();
		
		for(int i=0;i<num_samp;i++)
		{
			System.out.print(A[i]+"\t");
		}
		System.out.println();
		
		for(int i=0;i<num_samp;i++)
		{
			System.out.print(B[i+shift[i]]+"\t");
		}
		System.out.println();
		
	}
	
	private static float metric(float x,float y)
	{
		return (float)Math.pow(x-y,2);
	}

}