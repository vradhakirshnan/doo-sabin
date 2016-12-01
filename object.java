package data;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;


import data.point;
import data.face;




public class object{
	int num_of_points,num_of_faces, num_of_edjes;
	ArrayList points;
	int faces[][];
	boolean formed;
	double max_x=(double)0, max_y=(double)0;
	double min_x=(double)0, min_y=(double)0;

	public int ret_num_faces(){return this.num_of_faces;}
	public int ret_num_points(){return this.num_of_points;}
	public int ret_num_edjes(){return this.num_of_edjes;}
	public ArrayList ret_points(){return this.points;}
	public int[][] ret_faces(){return this.faces;}
	public double ret_max_x(){return this.max_x;}
	public double ret_max_y(){return this.max_y;}
	public double ret_min_x(){return this.min_x;}
	public double ret_min_y(){return this.min_y;}
	double get_x(int i)
	{
		double x = ((point)this.points.get(i)).ret_x();
		double z = ((point)this.points.get(i)).ret_z();
		return conv(x,z);

	}
	double get_y(int i)
	{
		double y = -((point)this.points.get(i)).ret_y();
		double z = -((point)this.points.get(i)).ret_z();
		return conv(y,z);
	}

/**********************************************/
	object(int num_of_points,int num_of_faces,int num_of_edjes, ArrayList points, int[][] faces )
	{
		this.num_of_points=num_of_points;
		this.num_of_faces=num_of_faces;
		this.num_of_edjes=num_of_edjes;
		this.points = points;
		this.faces = faces;
		this.formed =false;
		bounding_box();

	}
/**********************************************/
	public object(String Filename)
	{
		try{
				FileInputStream fis = new FileInputStream(Filename);
				InputStreamReader in = new InputStreamReader((InputStream)fis);
				BufferedReader br = new BufferedReader(in);
				String off =(br.readLine()).trim();
				if(off.equals("OFF"))
				{
					String ln=(br.readLine()).trim();
					ArrayList a = split_string(ln);
					this.num_of_points = Integer.parseInt((String)a.get(0));
					this.num_of_faces = Integer.parseInt((String)a.get(1));
					this.num_of_edjes = Integer.parseInt((String)a.get(2));
					this.points =  new ArrayList();
					this.faces = new int[this.num_of_faces][];
					for(int i=0;i<this.num_of_points;i++)
					{
						String pts = (br.readLine()).trim();
						ArrayList ps = split_string(pts);
						float x = Float.parseFloat((String)ps.get(0));
						float y = Float.parseFloat((String)ps.get(1));
						float z = Float.parseFloat((String)ps.get(2));
						point p = new point(x,y,z);
						//p.print_point();
						this.points.add(p);
					}
					for(int i=0;i<this.num_of_faces;i++)
					{
						String fcs= (br.readLine()).trim();
						ArrayList fs = split_string(fcs);
						int np =  Integer.parseInt((String)fs.get(0));
						this.faces[i]= new int[np+1];
						this.faces[i][0]=np;
						//System.out.println(this.faces[i][0]);
						for(int j=1;j<=np;j++)
						{
						 	this.faces[i][j]=Integer.parseInt((String)fs.get(j));
						// 	System.out.print(this.faces[i][j]+ " ");

						}
						//System.out.println();
					}
					this.formed =true;
					//subdivide();
					bounding_box();

				}
				else
				{
					System.out.println("not an off file..");
					this.formed =false;
				}
			}
		catch(Exception e){	e.printStackTrace();}
	}
/**********************************************/
public double conv(double x1,double z1)
{
	double pi = 3.1415926535;
	double  rad  = (float)(pi/(float)4);
	return (double )((double )x1+((double )z1*(double )Math.cos(rad)));

}
void bounding_box()
{
	for(int i=0;i< this.num_of_points;i++)
	{
		double tempx =  conv(((point)this.points.get(i)).ret_x(),((point)this.points.get(i)).ret_z());
		double tempy =  conv(((point)this.points.get(i)).ret_y(),((point)this.points.get(i)).ret_z());
		if(tempx >max_x) max_x =  tempx;
		if(tempy >max_y) max_y =  tempy;
		if(tempx <min_x) min_x =  tempx;
		if(tempy <min_y) min_y =  tempy;
	}
	//System.out.println("\n" + max_x + "\t" + max_y + "\t" + min_x + "\t" + min_y + "\n");

}



/**********************************************/
	private ArrayList split_string(String s)
	{
		ArrayList sub = new ArrayList();
		//System.out.println(s);
		int start_pos =s.indexOf(" ");
		//System.out.println(s);
		//System.out.println(start_pos);
		while(start_pos !=-1)
		{
			String str = s.substring(0,start_pos).trim();
			if(sub.add(str));//System.out.println(str);
			s = (s.substring(start_pos)).trim();
			start_pos =s.indexOf(" ");
			//System.out.println(s);
			//System.out.println(start_pos);
		}
		if(sub.add(s));//System.out.println(s);
		return  sub;
	}

/**********************************************/
	face form_face(int pos)
	{
		face f = new face();
		System.out.println(pos);
		for(int i=1;i<=this.faces[pos][0];i++)
		{
			f.add_point((point)this.points.get(this.faces[pos][i]));
		}
		f.add_point((point)this.points.get(this.faces[pos][1]));
		return f;
	}
/**********************************************/
	public void edjenumber(int arr[][])
	{
		int num=0;
		for(int i=0;i<this.num_of_points;i++)
		for(int j=0;j<this.num_of_points;j++)arr[i][j]=0;

		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<this.faces[i][0];j++)
			{
				int pos1=faces[i][j];
				int pos2=faces[i][j+1];
				arr[pos1][pos2]=1;
				if(arr[pos2][pos1]==0)
				{
					num++;
				}

			}
			//check last edje...
			int pos2=faces[i][1];
			int pos1=faces[i][this.faces[i][0]];
			arr[pos1][pos2]=1;
			if(arr[pos2][pos1]==0)
			{
				num++;
			}

		}
		this.num_of_edjes =  num;
		System.out.println("edjes: "+num);


	}


	/**********************************************/
	//find face for edje p-q
	void find_face(int p, int q,int arr[])
	{
		boolean got = false;
		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<this.faces[i][0];j++)
			{
				if(this.faces[i][j]==p && this.faces[i][j+1]==q)
				{
					arr[0]=i;
					arr[1]=j;
					got = true;
					break;
				}

			}
			if(got) break;
			if(this.faces[i][this.faces[i][0]]==p && this.faces[i][1]==q)
			{
				arr[0]=i;
				arr[1]=this.faces[i][0];
				got = true;
				break;
			}

		}
	}

/**********************************************/

	public object  subdivide()
	{
		//number of points, edjes and faces for hte new object....
		int pts,fcs=0,eds;
		ArrayList face_points = new ArrayList();
		int[] points = new int[this.num_of_points];
		int[][] face_point_index;
		int face_index = 0;
		int edjes[][]= new int[this.num_of_points][this.num_of_points];
		edjenumber(edjes);
		face_point_index = new int[this.num_of_faces + this.num_of_edjes + this.num_of_points][];
		for(int i=0;i<this.num_of_points;i++)points[i]=1;
		//get face points for all faces...
		//generate faces points...
		//form faces from faces...
		for(int i=0;i<this.num_of_faces;i++)
		{
			int length = this.faces[i][0];
			face_point_index [i]=new int[length+1];
			face f =form_face(i);
			face newface = f.get_face();
			face_point_index [i][0]=f.ret_num();
			fcs++;
			for(int j=0;j<f.ret_num();j++)
			{
				point face_point = (point)newface.ret_point(j);
				if(face_points.add(face_point));
				int index = face_points.indexOf(face_point);
				face_point_index[i][j+1]=index;

			}
		}
		//for(int i=0;i<this.num_of_faces;i++)
		//{
		//	for(int j=0;j<=this.faces[i][0];j++)
		//	{
			//	System.out.print(face_point_index[i][j]+" ");
		//	}
			//System.out.println();
		//}


		//vertex faces....
		face_index = this.num_of_faces;
		for(int i=0;i<this.num_of_points;i++)
		{
			//System.out.println(i +"th point");
			int p_num=0;
			for(int j=0;j<this.num_of_points;j++)
			{
				if(edjes[i][j]==1)p_num++;
			}
			//System.out.println(p_num + "faces/edjes containng point");
			face_point_index[face_index]= new int[p_num+1];
			face_point_index[face_index][0]=p_num;
			int[] arr= new int[2];
			int pos=0;
			while(edjes[i][pos]==0)pos++;
			find_face(i,pos,arr);
			//System.out.println(arr[0] + "  " + arr[1]);
			face_point_index[face_index][1]=face_point_index[arr[0]][arr[1]];
			p_num--;
			int num=2;
			if(p_num>=2)
			{
				//System.out.println(p_num + " faces/edjes containng point --- 2nd ");
				while(p_num>0)
				{
					int length =this.faces[arr[0]][0];
					if(arr[1]==1) pos=this.faces[arr[0]][length];
					else pos=this.faces[arr[0]][arr[1]-1];
					find_face(i,pos,arr);
					//System.out.println("new arr\n" + arr[0] + "  " + arr[1]);
					face_point_index[face_index][num]=face_point_index[arr[0]][arr[1]];
					p_num--;
					num++;
				}
				//System.out.println("\n");
				face_index++;
				fcs++;
			}
		}

		//edje faces....
		for(int i=0;i<this.num_of_points;i++)
		{
			for(int j=0;j<this.num_of_points;j++)
			{
				if(edjes[i][j]==1 && edjes[j][i]==1)
				{

					int pos;
					face_point_index[face_index]= new int[5];
					int[] arr= new int[2];
					find_face(i,j,arr);
					//System.out.println("\n" + arr[0] + "  " + arr[1]);
					int length =this.faces[arr[0]][0];
					if(arr[1]==length)pos=1;
					else pos=arr[1]+1;
					face_point_index[face_index][0]=4;
					face_point_index[face_index][1]=face_point_index[arr[0]][pos];
					face_point_index[face_index][2]=face_point_index[arr[0]][arr[1]];
					find_face(j,i,arr);
					//System.out.println(arr[0] + "  " + arr[1]);
					length =this.faces[arr[0]][0];
					if(arr[1]==length)pos=1;
					else pos=arr[1]+1;
					face_point_index[face_index][3]=face_point_index[arr[0]][pos];
					face_point_index[face_index][4]=face_point_index[arr[0]][arr[1]];
					edjes[i][j]=0;
					edjes[j][i]=0;
					face_index++;
					fcs++;
				}

			}

		}

		System.out.println("\n............................\noff file output");
		//System.out.println("number of points...\t" + pts);
		pts = face_points.size();
		System.out.println("number of points...\t" + pts);
		System.out.println("number of faces...\t" + fcs);

		//fcs = this.num_of_points + this.num_of_faces +this.num_of_edjes;
		eds=0;
		object newobject = new object(face_points.size(),fcs,eds,face_points,face_point_index);
		int ed[][] = new int[newobject.ret_num_points()][newobject.ret_num_points()];
		//newobject.edjenumber(ed);
		return newobject;
	}

/**********************************************/

	public void save(String Filename)
	{
		//save into off file....
		if(this.formed)System.out.println("already saved...");
		else
		{
			try{
				FileOutputStream fos = new FileOutputStream (Filename);

				PrintStream pr =new PrintStream((OutputStream)fos);
				int edjes[][]= new int[this.num_of_points][this.num_of_points];
				edjenumber(edjes);
				pr.println("OFF");
				pr.print(this.num_of_points +" " + this.num_of_faces + " " + this.num_of_edjes);
				pr.println();
				for(int i=0;i<this.num_of_points;i++)
				{
					double x = ((point)this.points.get(i)).ret_x();
					double y = ((point)this.points.get(i)).ret_y();
					double z = ((point)this.points.get(i)).ret_z();
					pr.println(x +" " + y + " " + z);
				}
				for(int i=0;i<this.num_of_faces;i++)
				{

					for(int j=0;j<=this.faces[i][0];j++)
					{
						pr.print( this.faces[i][j] +" ");
					}
					pr.println();
				}
				pr.close();
				this.formed =true;


			}

			catch(Exception e){e.printStackTrace();}
		}

	}

/**********************************************/
	public void plot_object(Graphics2D g2)
	{
		double min;
		double trans  = (double)300;
		if((max_x -min_x)>(max_y -min_y)) min =(max_x -min_x);
		else min =(max_y -min_y);
		g2.setPaint(Color.black);
		g2.translate(300,300);
		//g2.rotate(3.1415926535);
		double scale = (double)200/min;
		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<this.faces[i][0];j++)
			{
				//double x1 = trans + get_x(this.faces[i][j])* scale;
				//double y1 = get_y(this.faces[i][j])* scale + trans;
				//double x2 = trans + get_x(this.faces[i][j+1])* scale;
				//double y2 = get_y(this.faces[i][j+1])* scale + trans;
				double x1 = get_x(this.faces[i][j])* scale;
				double y1 = get_y(this.faces[i][j])* scale;
				double x2 = get_x(this.faces[i][j+1])* scale;
				double y2 = get_y(this.faces[i][j+1])* scale;
				Line2D l =  new Line2D.Double(x1,y1,x2,y2);
				g2.draw(l);
			}
			double x1 = get_x(this.faces[i][this.faces[i][0]]) * scale;
			double y1 = get_y(this.faces[i][this.faces[i][0]])* scale ;
			double x2 = get_x(this.faces[i][1])* scale;
			double y2 = get_y(this.faces[i][1])* scale;
			Line2D l =  new Line2D.Double(x1,y1,x2,y2);
			g2.draw(l);

		}

	}
	public static void main(String args[]) throws Exception
	{
		String Filename1 = args[0];
		/*final object o = new object(Filename1);
		final object o2 =o.subdivide();
		String Filename2 = args[1];
		o2.save(Filename2);
		JFrame f = new JFrame("object"){
			public void paint(Graphics g)
			{
				Graphics2D g2 = (Graphics2D)g;
				//o.plot_object(g2);
				o2.plot_object(g2);
			}

		};
		f.setSize(700,700);
		f.setVisible(true);*/
	String s = " 2                  0                1";
	final object o = new object(Filename1);
	ArrayList arr = o.split_string(s);
	for(int i = 0;i<arr.size();i++)
	{
		System.out.println((String)arr.get(i));
	}


	}
}
