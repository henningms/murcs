using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Murcs.Resources;
using Microsoft.WindowsAzure.MobileServices;
using Murcs.Models;
using System.Windows.Threading;

namespace Murcs
{
    public partial class MainPage : PhoneApplicationPage
    {
        private DispatcherTimer onlineCollaboratorsTimer;

        // Constructor
        public MainPage()
        {
            InitializeComponent();

            Loaded += MainPage_Loaded;
            
            // Sample code to localize the ApplicationBar
            //BuildLocalizedApplicationBar();
        }

        async void MainPage_Loaded(object sender, RoutedEventArgs e)
        {
            await Authenticate();

            onlineCollaboratorsTimer = new DispatcherTimer();

            onlineCollaboratorsTimer.Interval = TimeSpan.FromSeconds(10);
            onlineCollaboratorsTimer.Tick += (s, ev) =>
            {
                UpdateOnlineCollaborators();
            };

            onlineCollaboratorsTimer.Start();

            UpdateData();
        }

        private void UpdateData()
        {
            UpdateOnlineCollaborators();
            UpdateTasks();
            UpdateTasksContributedByMe();
        }

        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
            base.OnNavigatedFrom(e);

            if (onlineCollaboratorsTimer != null)
            {
                onlineCollaboratorsTimer.Stop();
            }

            App.CurrentChannel.HttpNotificationReceived += CurrentChannel_HttpNotificationReceived;
                
        }

        void CurrentChannel_HttpNotificationReceived(object sender, Microsoft.Phone.Notification.HttpNotificationEventArgs e)
        {
            Dispatcher.BeginInvoke(() =>
            {
                UpdateData();
            });
            
        }

        private async System.Threading.Tasks.Task Authenticate()
        {
            while (App.MobileServiceUser == null)
            {
                string message;
                try
                {
                    App.MobileServiceUser = await App.MobileService
                        .LoginAsync(MobileServiceAuthenticationProvider.Facebook);
                    message =
                        string.Format("You are now logged in - {0}", App.MobileServiceUser.UserId);
                }
                catch (InvalidOperationException)
                {
                    message = "You must log in. Login Required";
                }
            }

            SetUserStatus(true);
            RegisterForNotifications();
        }

        private async void SetUserStatus(bool online)
        {
            var status = online == true ? "online" : "offline";

            try
            {
                await App.MobileService.InvokeApiAsync("user_status", System.Net.Http.HttpMethod.Put, new Dictionary<string, string>()
                    {
                        { "status", status}
                    });
            }
            catch (Exception ex)
            {
                LogDebug("SetUserStatus failed");
            }
        }

        private static void LogDebug(string message)
        {
            System.Diagnostics.Debug.WriteLine(message);
        }

        private async void RegisterForNotifications()
        {
            var push = new Push {
                Platform = "wp",
                Identifier = App.CurrentChannel.ChannelUri.ToString()
            };

            try
            {
                await App.MobileService.GetTable<Push>().InsertAsync(push);
            }
            catch (Exception ex)
            {
                LogDebug("RegisterForNotifications failed");
            }
        }

        private void AddTaskAppBarButton_Click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/AddTask.xaml", UriKind.Relative));
        }

        private async void UpdateOnlineCollaborators()
        {
            try
            {
                var collaborators = App.MobileService.GetTable<User>().Where(user => user.Status == "online");

                OnlineCollaboratorsListBox.ItemsSource = await collaborators.ToCollectionAsync();
            }
            catch (Exception ex)
            {
                LogDebug("UpdateOnlineCollaborators failed");
            }
        }

        private async void UpdateTasks()
        {
            try
            {
                var tasks = App.MobileService.GetTable<Task>().OrderByDescending(task => task.CreatedAt);

                TasksListBox.ItemsSource = await tasks.ToCollectionAsync();
            }
            catch (Exception ex)
            {
                LogDebug("UpdateTasks failed");
            }
        }

        private async void UpdateTasksContributedByMe()
        {
            try
            {

                var tasks = App.MobileService.GetTable<Task>()
                    .Where(task => task.UserId == App.MobileService.CurrentUser.UserId)
                    .OrderBy(task => task.CreatedAt);

                ContributedTasksListBox.ItemsSource = await tasks.ToCollectionAsync();
            }
            catch (Exception ex)
            {
                LogDebug("UpdatetasksContributedByMe failed");
            }
        }
    }
}