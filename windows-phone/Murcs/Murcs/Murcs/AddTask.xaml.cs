using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Murcs.Models;

namespace Murcs
{
    public partial class AddTask : PhoneApplicationPage
    {
        public AddTask()
        {
            InitializeComponent();
        }

        private void SaveTaskAppBarButton_Click(object sender, EventArgs e)
        {
            SaveTask();
        }

        private async void SaveTask()
        {
            var taskText = TaskDescriptionTextBox.Text;

            var task = new Task
            {
                Text = taskText
            };


            await App.MobileService.GetTable<Task>().InsertAsync(task);

            NavigationService.GoBack();
        }
    }
}