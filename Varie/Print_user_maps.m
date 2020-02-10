close all
clearvars
clc
x = [6
65
15
43
8
34
10
1
77
21
21
45
1
42
23
65
1
45
9
74
-20
10
100
60];
y = [3
41
4
3
4
5
4
30
89
100
12
7
65
39
38
19
1
17
54
63
-20
40
10
5];
raggi = [5
8
32
2
3
9
3
21
3
9
20
14
1
7
33
6
1
12
25
35
15
22
30
15];
% 
% [x y]
% %scatter(x(:), y(:), raggi(:).^2)
% viscircles([x y],raggi)

figure
colors = {'b','r','g','y','k'};
label = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Pippo', 'Q', 'R', 'S', 'T', 'U', 'V', 'Z'};

for k = 1:1
    % Create 5 random circles to display,
    X = x
    Y = y
    centers = [X Y];
    radii = raggi;

    % Clear the axes.
    cla

    % Fix the axis limits.
    xlim([-50 120])
    ylim([-50 120])

    % Set the axis aspect ratio to 1:1.
    axis square

    % Set a title.
    title(['k = ' num2str(k)])

    % Display the circles.
    viscircles(centers,radii,'Color',colors{k}, 'LineStyle','-','LineWidth',0.5);
    hold on
    scatter(x(:), y(:),5,'k','+')
    labelpoints(x,y,label)
    centro = [((10 + 100)/2) ((4 + 10)/2)];
    viscircles(centro,45,'Color',colors{2}, 'LineStyle','-','LineWidth',1);
    scatter(centro(1),centro(2),5,'k','*')
    % Pause for 1 second.
    pause(1)
    
end
